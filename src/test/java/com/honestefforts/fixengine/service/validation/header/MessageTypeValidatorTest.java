package com.honestefforts.fixengine.service.validation.header;

import static com.honestefforts.fixengine.model.message.tags.TagType.STRING;
import static org.assertj.core.api.Assertions.assertThat;

import com.honestefforts.fixengine.model.message.tags.RawTag;
import com.honestefforts.fixengine.model.validation.ValidationError;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MessageTypeValidatorTest {

  @ParameterizedTest
  @CsvSource({"D"})
  void validate_happyPath(String type) {
    ValidationError validationResult = MessageTypeValidator.validate(
        RawTag.builder().tag(35).dataType(STRING).value(type).position(3).build());

    assertThat(validationResult).usingRecursiveComparison().withStrictTypeChecking()
        .isEqualTo(ValidationError.empty());
    assertThat(validationResult.hasErrors()).isFalse();
  }

  @Test
  void validate_notThirdInMessage_expectValidationError() {
    RawTag tag = RawTag.builder().tag(35).dataType(STRING).value("D").position(2).build();
    ValidationError validationResult = MessageTypeValidator.validate(tag);

    assertThat(validationResult).usingRecursiveComparison().withStrictTypeChecking()
        .isEqualTo(ValidationError.builder().submittedTag(tag).critical(true)
            .error("MsgType (35) tag must be the third tag in the message!").build());
  }

  @Test
  void validate_invalidType_expectValidationError() {
    RawTag tag = RawTag.builder().tag(35).dataType(STRING).value("ABCD").position(3).build();
    ValidationError validationResult = MessageTypeValidator.validate(tag);

    assertThat(validationResult).usingRecursiveComparison().withStrictTypeChecking()
        .isEqualTo(ValidationError.builder().submittedTag(tag).critical(true)
            .error("Message type is invalid!").build());
  }

  @Test
  void validate_customType_expectValidationError() {
    RawTag tag = RawTag.builder().tag(35).dataType(STRING).value("UXX").position(3).build();
    ValidationError validationResult = MessageTypeValidator.validate(tag);

    assertThat(validationResult).usingRecursiveComparison().withStrictTypeChecking()
        .isEqualTo(ValidationError.builder().submittedTag(tag).critical(true)
            .error("Unknown private message format!").build());
  }

  @Test
  void validate_unsupportedType_expectValidationError() {
    RawTag tag = RawTag.builder().tag(8).dataType(STRING).value("BH").position(3).build();
    ValidationError validationResult = MessageTypeValidator.validate(tag);

    assertThat(validationResult).usingRecursiveComparison().withStrictTypeChecking()
        .isEqualTo(ValidationError.builder().submittedTag(tag).critical(true)
            .error("Message Type is not currently supported!").build());
  }

}
