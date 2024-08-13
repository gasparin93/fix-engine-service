package com.honestefforts.fixengine.service.validation;

import static com.honestefforts.fixengine.model.message.enums.MessageType.NEW_ORDER_SINGLE;
import static com.honestefforts.fixengine.model.validation.FixValidator.EMPTY_OR_NULL_VALUE;
import static com.honestefforts.fixengine.model.validation.FixValidator.REQUIRED_ERROR_MSG;
import static com.honestefforts.fixengine.service.TestUtility.getContext;
import static com.honestefforts.fixengine.service.TestUtility.getRawTag;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.honestefforts.fixengine.model.message.tags.RawTag;
import com.honestefforts.fixengine.model.validation.FixValidator;
import com.honestefforts.fixengine.model.validation.ValidationError;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FixValidatorFactoryTest {

  private static final int A_REQUIRED_TAG = 11;
  private static final int AN_OPTIONAL_TAG = 526;
  private static final int A_TAG = 17;

  @Mock
  FixValidator validator;
  FixValidatorFactory factory;

  @Test
  void validateTag_tagSpecificValidation_happyPath() {
    when(validator.supports()).thenReturn(A_TAG);
    factory = new FixValidatorFactory(List.of(validator));

    when(validator.applicableToMessageType(any())).thenReturn(true);
    when(validator.validate(any(), any())).thenReturn(ValidationError.empty());

    ValidationError validationError = factory.validateTag(getRawTag(A_TAG, "text"),
        getContext(NEW_ORDER_SINGLE));

    assertThat(validationError.isEmpty()).isTrue();
    verify(validator, times(1)).validate(any(), any());
  }

  @Test
  void validateTag_genericTypeBasedValidation_happyPath() {
    when(validator.supports()).thenReturn(AN_OPTIONAL_TAG);
    factory = new FixValidatorFactory(List.of(validator));

    ValidationError validationError = factory.validateTag(getRawTag(A_TAG, "text"),
        getContext(NEW_ORDER_SINGLE));

    assertThat(validationError.isEmpty()).isTrue();
    verify(validator, times(0)).validate(any(), any());
  }

  @Test
  void validateTag_invalidTag_expectValidationError() {
    when(validator.supports()).thenReturn(A_TAG);
    factory = new FixValidatorFactory(List.of(validator));

    RawTag tag = getRawTag(9999, "text");
    ValidationError validationError = factory.validateTag(tag, getContext(NEW_ORDER_SINGLE));

    assertThat(validationError).usingRecursiveComparison().withStrictTypeChecking()
        .isEqualTo(ValidationError.builder().submittedTag(tag).error("Unsupported tag").build());
  }

  @ParameterizedTest
  @CsvSource(value= {"null,"}, nullValues={"null"})
  void validateTag_tagSpecificValidationWithNullOrEmptyRequiredValue_expectValidationError(
      String value) {
    when(validator.supports()).thenReturn(11);
    factory = new FixValidatorFactory(List.of(validator));
    when(validator.applicableToMessageType(any())).thenReturn(true);

    RawTag tag = getRawTag(11, value);
    ValidationError validationError = factory.validateTag(tag, getContext(NEW_ORDER_SINGLE));

    assertThat(validationError).usingRecursiveComparison().withStrictTypeChecking()
        .isEqualTo(ValidationError.builder().submittedTag(tag).critical(true)
            .error(REQUIRED_ERROR_MSG).build());

    verify(validator, times(1)).applicableToMessageType(any());
  }

  @ParameterizedTest
  @CsvSource(value= {
      "null,true",",true",
      "null,false",",false"
  }, nullValues={"null"})
  void validateTag_tagSpecificValidationWithNullOrEmptyRequiredValue_expectValidationError(
      String value, boolean isRequired) {
    Integer tagUsed = isRequired ? A_REQUIRED_TAG : AN_OPTIONAL_TAG;
    when(validator.supports()).thenReturn(tagUsed);
    factory = new FixValidatorFactory(List.of(validator));
    when(validator.applicableToMessageType(any())).thenReturn(true);

    RawTag tag = getRawTag(tagUsed, value);
    ValidationError validationError = factory.validateTag(tag, getContext(NEW_ORDER_SINGLE));

    assertThat(validationError).usingRecursiveComparison().withStrictTypeChecking()
        .isEqualTo(ValidationError.builder().submittedTag(tag).critical(isRequired)
            .error(isRequired ? REQUIRED_ERROR_MSG : EMPTY_OR_NULL_VALUE).build());

    verify(validator, times(1)).applicableToMessageType(any());
  }

  @ParameterizedTest
  @CsvSource(value= {
      "null,true",",true",
      "null,false",",false"
  }, nullValues={"null"})
  void validateTag_genericValidationWithNullOrEmptyRequiredValue_expectValidationError(
      String value, boolean isRequired) {
    when(validator.supports()).thenReturn(A_TAG);
    factory = new FixValidatorFactory(List.of(validator));

    RawTag tag = getRawTag(isRequired ? A_REQUIRED_TAG : AN_OPTIONAL_TAG, value);
    ValidationError validationError = factory.validateTag(tag, getContext(NEW_ORDER_SINGLE));

    assertThat(validationError).usingRecursiveComparison().withStrictTypeChecking()
        .isEqualTo(ValidationError.builder().submittedTag(tag).critical(isRequired)
            .error(isRequired ? REQUIRED_ERROR_MSG : EMPTY_OR_NULL_VALUE).build());

    verify(validator, times(0)).applicableToMessageType(any());
  }

  @ParameterizedTest
  @CsvSource({"101, 261, 809, 9999, -1"})
  void isASupportedTag_unsupportedTags_expectFalse(Integer tag) {
    assertThat(FixValidatorFactory.isASupportedTag(getRawTag(tag, ""))).isFalse();
  }

}
