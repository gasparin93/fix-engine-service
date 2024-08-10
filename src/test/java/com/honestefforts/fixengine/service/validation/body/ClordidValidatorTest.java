package com.honestefforts.fixengine.service.validation.body;

import static com.honestefforts.fixengine.service.TestUtility.getRawTag;
import static com.honestefforts.fixengine.service.TestUtility.getRawTagEntry;
import static com.honestefforts.fixengine.service.TestUtility.parseDateTimeToString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.honestefforts.fixengine.model.message.FixMessageContext;
import com.honestefforts.fixengine.model.message.tags.RawTag;
import com.honestefforts.fixengine.model.validation.ValidationError;
import com.honestefforts.fixengine.service.model.BloomFilterWrapper;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ClordidValidatorTest {

  @Mock
  BloomFilterWrapper bloomFilter;

  @InjectMocks
  ClordidValidator validator;

  @Test
  void validate_happyPath() {
    when(bloomFilter.mightContain(any())).thenReturn(false);

    ValidationError validationResult = validator.validate(
        getRawTag(11, "clordid123"),
        FixMessageContext.builder()
            .messageType("D")
            .processedMessages(Map.ofEntries(
                getRawTagEntry(60, parseDateTimeToString(LocalDateTime.now()))))
            .build()
    );

    assertThat(validationResult.hasErrors()).isFalse();
  }

  @Test
  void validate_incomingMessageIsDifferent_dateIsUpdatedAndBloomFilterIsReset() {
    when(bloomFilter.mightContain(any())).thenReturn(false);

    LocalDate initialDate = validator.getCurrentDay();
    ValidationError validationResult = validator.validate(
        getRawTag(11, "clordid123"),
        FixMessageContext.builder()
            .messageType("D")
            .processedMessages(Map.ofEntries(
                getRawTagEntry(60, parseDateTimeToString(LocalDateTime.now().plusWeeks(1)))))
            .build()
    );

    assertThat(validationResult.hasErrors()).isFalse();
    assertThat(initialDate).isNotEqualTo(validator.getCurrentDay());
    verify(bloomFilter, times(1)).reset();
  }

  @Test
  void validate_clordidIsAlreadyUsed_expectValidationError() {
    when(bloomFilter.mightContain(any())).thenReturn(true);

    ValidationError validationResult = validator.validate(
        getRawTag(11, "clordid123"),
        FixMessageContext.builder()
            .messageType("D")
            .processedMessages(Map.ofEntries(
                getRawTagEntry(60, parseDateTimeToString(LocalDateTime.now().plusWeeks(1)))))
            .build()
    );

    assertThat(validationResult).usingRecursiveComparison().withStrictTypeChecking()
        .isEqualTo(ValidationError.builder().critical(true)
            .submittedTag(getRawTag(11, "clordid123"))
            .error("ClOrdID (tag 11) must be unique for a single trade date!")
            .build());
  }

  @ParameterizedTest
  @MethodSource("invalidTransactDate")
  void validate_transactDateIsInvalid_expectNoError(Map.Entry<Integer, RawTag> transactDateEntry) {
    //transactDate validator will yield a critical error instead
    //TODO: there should be an integrated test to ensure that this is the case

    ValidationError validationResult = validator.validate(
        getRawTag(11, "clordid123"),
        FixMessageContext.builder()
            .messageType("D")
            .processedMessages(
                Optional.ofNullable(transactDateEntry).map(Map::ofEntries).orElse(Map.of()))
            .build()
    );

    assertThat(validationResult.hasErrors()).isFalse();
  }

  @Test
  void supports_tag11() {
    assertThat(validator.supports()).isEqualTo(11);
  }

  @ParameterizedTest
  @CsvSource({"D, true",
              "A, false"})
  void applicableToMessageType(String messageType, boolean isSupported) {
    assertThat(validator.applicableToMessageType(messageType))
        .isEqualTo(isSupported);
  }

  private static Stream<Arguments> invalidTransactDate() {
    return Stream.of(
        null,
        Arguments.of(getRawTagEntry(60, null)),
        Arguments.of(getRawTagEntry(60, "")),
        Arguments.of(getRawTagEntry(60, "invalid date"))
    );
  }

}
