package com.honestefforts.fixengine.service.validation.body;

import static com.honestefforts.fixengine.model.message.enums.MessageType.NEW_ORDER_SINGLE;
import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseUtcTimestamp;

import com.honestefforts.fixengine.model.message.FixMessageContext;
import com.honestefforts.fixengine.model.message.enums.MessageType;
import com.honestefforts.fixengine.model.message.tags.RawTag;
import com.honestefforts.fixengine.model.validation.FixValidator;
import com.honestefforts.fixengine.model.validation.ValidationError;
import com.honestefforts.fixengine.service.model.BloomFilterWrapper;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
public class ClordidValidator implements FixValidator {
  private static final Set<MessageType> applicableMessageTypes = Set.of(NEW_ORDER_SINGLE);
  private final BloomFilterWrapper bloomFilter;
  private final ReentrantLock lock = new ReentrantLock();
  @Getter
  private LocalDate currentDay = LocalDate.now();

  public ClordidValidator(BloomFilterWrapper bloomFilter) {
    this.bloomFilter = bloomFilter;
  }

  @Override
  public ValidationError validate(final RawTag rawTag, final FixMessageContext context) {
    return Optional.ofNullable(context.getValueForTag(60))
        .map(ClordidValidator::parseUtcTimestampAndCatchExceptions)
        .map(LocalDateTime::toLocalDate)
        .map(transactDate -> {
          lock.lock();
          try {
            if (!transactDate.equals(currentDay)) {
              bloomFilter.reset();
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

  private static LocalDateTime parseUtcTimestampAndCatchExceptions(String transactDateStr) {
    try {
      return parseUtcTimestamp(transactDateStr);
    } catch(Exception e) {
      return null;
    }
  }

  @Override
  public Integer supports() {
    return 11;
  }

  @Override
  public boolean applicableToMessageType(MessageType messageType) {
    return applicableMessageTypes.contains(messageType);
  }

}
