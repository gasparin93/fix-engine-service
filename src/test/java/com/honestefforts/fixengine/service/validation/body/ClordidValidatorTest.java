package com.honestefforts.fixengine.service.validation.body;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

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
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@ExtendWith(MockitoExtension.class)
public class ClordidValidatorTest {

  @Mock
  BloomFilter<String> bloomFilter;
  @Mock
  ClordidValidationConfig clordidValidationConfig;
  @Mock
  ReentrantLock lock;
  @Mock
  LocalDate currentDay;

  @InjectMocks
  ClordidValidator validator;

/*  @Autowired
  private ClordidValidatorTest(ClordidValidationConfig clordidValidationConfig) {
    this.clordidValidationConfig = clordidValidationConfig;
    initializeBloomFilter();
    this.currentDay = LocalDate.now();
    this.lock = new ReentrantLock();
  }*/

 /* public ValidationError validate(final RawTag rawTag, final FixMessageContext context) {
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
  }*/

  @Test
  void supports_tag11() {
    assertThat(validator.supports()).isEqualTo(11);
  }

  @ParameterizedTest
  @CsvSource({"D, true",
              "A, false"})
  void applicableToMessageType(String messageType, boolean isSupported) {
/*    when(clordidValidationConfig.getExpectedInsertions()).thenReturn(10);
    when(clordidValidationConfig.getFalsePositiveProbability()).thenReturn(.01);
    validator = new ClordidValidator(clordidValidationConfig);*/
    assertThat(validator.applicableToMessageType(messageType))
        .isEqualTo(isSupported);
  }

}
