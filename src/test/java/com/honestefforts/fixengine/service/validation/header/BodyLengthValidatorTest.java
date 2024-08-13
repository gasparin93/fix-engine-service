package com.honestefforts.fixengine.service.validation.header;

import static com.honestefforts.fixengine.model.message.enums.MessageType.NEW_ORDER_SINGLE;
import static com.honestefforts.fixengine.model.message.tags.TagType.POSITIVE_INTEGER;
import static com.honestefforts.fixengine.service.TestUtility.getContext;
import static org.assertj.core.api.Assertions.assertThat;

import com.honestefforts.fixengine.model.message.enums.MessageType;
import com.honestefforts.fixengine.model.message.tags.RawTag;
import com.honestefforts.fixengine.model.validation.ValidationError;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class BodyLengthValidatorTest {

  BodyLengthValidator validator = new BodyLengthValidator();

  @Test
  void validate_happyPath() {
    ValidationError validationResult = validator.validate(
        RawTag.builder().tag(9).dataType(POSITIVE_INTEGER).value("1").position(2).build(),
        getContext(NEW_ORDER_SINGLE));

    assertThat(validationResult.hasErrors()).isFalse();
  }

  @Test
  void validate_notSecondInMessage_expectValidationError() {
    RawTag tag = RawTag.builder().tag(9).dataType(POSITIVE_INTEGER).value("FIX.4.4").position(1).build();
    ValidationError validationResult = validator.validate(tag, getContext(NEW_ORDER_SINGLE));

    assertThat(validationResult).usingRecursiveComparison().withStrictTypeChecking()
        .isEqualTo(ValidationError.builder().submittedTag(tag).critical(true)
            .error("BodyLength (9) tag must be the second tag in the message!").build());
  }

  @ParameterizedTest
  @CsvSource({"-1", "1.5", "one"})
  void validate_invalidInputs_expectValidationError(String length) {
    RawTag tag = RawTag.builder().tag(9).dataType(POSITIVE_INTEGER).value(length).position(2).build();
    ValidationError validationResult = validator.validate(tag, getContext(NEW_ORDER_SINGLE));;

    assertThat(validationResult).usingRecursiveComparison().withStrictTypeChecking()
        .isEqualTo(ValidationError.builder().submittedTag(tag).critical(true)
            .error("Tag violates expected format!").build());
  }

  @Test
  void supports_tag9() {
    assertThat(validator.supports()).isEqualTo(9);
  }

  @ParameterizedTest
  @CsvSource({"NEW_ORDER_SINGLE, true"})
  void applicableToMessageType(MessageType messageType, boolean isSupported) {
    assertThat(validator.applicableToMessageType(messageType)).isEqualTo(isSupported);
  }

}
