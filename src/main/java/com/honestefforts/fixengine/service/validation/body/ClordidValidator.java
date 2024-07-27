package com.honestefforts.fixengine.service.validation.body;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.honestefforts.fixengine.model.message.FixMessageContext;
import com.honestefforts.fixengine.model.message.tags.RawTag;
import com.honestefforts.fixengine.model.validation.FixValidator;
import com.honestefforts.fixengine.model.validation.ValidationError;
import com.honestefforts.fixengine.service.config.ClordidValidationConfig;
import com.honestefforts.fixengine.service.converter.util.CommonConversionUtil;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClordidValidator implements FixValidator {
  private BloomFilter<String> bloomFilter;
  private final ClordidValidationConfig clordidValidationConfig;
  private final ReentrantLock lock;
  private LocalDate currentDay;

  @Autowired
  private ClordidValidator(ClordidValidationConfig clordidValidationConfig) {
    this.clordidValidationConfig = clordidValidationConfig;
    initializeBloomFilter();
    this.currentDay = LocalDate.now();
    this.lock = new ReentrantLock();
  }

  @Override
  public ValidationError validate(final RawTag rawTag, final FixMessageContext context) {
    return Optional.ofNullable(context.getValueForTag(60))
        .map(CommonConversionUtil::parseUtcTimestamp)
        .map(LocalDateTime::toLocalDate)
        .map(transactDate -> {
          lock.lock();
          try {
            if (!transactDate.equals(currentDay)) {
              initializeBloomFilter();
              currentDay = transactDate;
            }
            if (bloomFilter.mightContain(rawTag.value())) {
              //TODO: this may be a false positive - come up with a way to verify
              //possible: db check - but this rules out elements being processed concurrently
              return ValidationError.builder().submittedTag(rawTag).critical(true)
                  .error("ClOrdID (tag 11) must be unique for a single trade date!")
                  .build();
            } else {
              bloomFilter.put(rawTag.value());
              return ValidationError.empty();
            }
          } finally {
            lock.unlock();
          }
        })
        .orElse(ValidationError.empty()); //transact date is invalid, that validator will yield critical error
  }

  private void initializeBloomFilter() {
    bloomFilter = BloomFilter.create(Funnels.unencodedCharsFunnel(),
        clordidValidationConfig.getExpectedInsertions(),
        clordidValidationConfig.getFalsePositiveProbability());
  }

  @Override
  public Integer supports() {
    return 11;
  }

}
