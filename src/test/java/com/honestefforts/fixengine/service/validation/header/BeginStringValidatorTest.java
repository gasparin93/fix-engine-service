package com.honestefforts.fixengine.service.validation.header;

import static com.honestefforts.fixengine.model.message.tags.TagType.STRING;
import static com.honestefforts.fixengine.service.TestUtility.getContext;
import static org.assertj.core.api.Assertions.assertThat;

import com.honestefforts.fixengine.model.message.FixMessageContext;
import com.honestefforts.fixengine.model.message.tags.RawTag;
import com.honestefforts.fixengine.model.validation.ValidationError;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class BeginStringValidatorTest {

  BeginStringValidator validator = new BeginStringValidator();

  @ParameterizedTest
  @CsvSource({"FIX.4.4"})
  void validate_happyPath(String version) {
    ValidationError validationResult = validator.validate(
        RawTag.builder().tag(8).dataType(STRING).value(version).position(1).build(),
        getContext("D"));

    assertThat(validationResult.hasErrors()).isFalse();
  }

  @Test
  void validate_notFirstInMessage_expectValidationError() {
    RawTag tag = RawTag.builder().tag(8).dataType(STRING).value("FIX.4.4").position(2).build();
    ValidationError validationResult = validator.validate(tag, getContext("D"));

    assertThat(validationResult).usingRecursiveComparison().withStrictTypeChecking()
        .isEqualTo(ValidationError.builder().submittedTag(tag).critical(true)
            .error("BeginString (8) tag must be the first tag in the message!").build());
  }

  @Test
  void validate_invalidVersion_expectValidationError() {
    RawTag tag = RawTag.builder().tag(8).dataType(STRING).value("ABCD").position(1).build();
    ValidationError validationResult = validator.validate(tag, getContext("D"));;

    assertThat(validationResult).usingRecursiveComparison().withStrictTypeChecking()
        .isEqualTo(ValidationError.builder().submittedTag(tag).critical(true)
            .error("FIX version is not valid!").build());
  }

  @Test
  void validate_unsupportedVersion_expectValidationError() {
    RawTag tag = RawTag.builder().tag(8).dataType(STRING).value("FIX.4.0").position(1).build();
    ValidationError validationResult = validator.validate(tag, FixMessageContext.builder()
        .messageType("D")
        .version("FIX.4.0")
        .processedMessages(Map.of())
        .build());

    assertThat(validationResult).usingRecursiveComparison().withStrictTypeChecking()
        .isEqualTo(ValidationError.builder().submittedTag(tag).critical(true)
            .error("FIX version is not currently supported!").build());
  }

  @Test
  void validate_mismatchingVersions_expectValidationError() {
    RawTag tag = RawTag.builder().tag(8).dataType(STRING).value("FIX.4.4").position(1).build();
    ValidationError validationResult = validator.validate(tag,
        FixMessageContext.builder()
            .messageType("D")
            .version("FIX.4.2")
            .processedMessages(Map.of())
            .build());

    assertThat(validationResult).usingRecursiveComparison().withStrictTypeChecking()
        .isEqualTo(ValidationError.builder().submittedTag(tag).critical(true)
            .error("FIX version in message does not match the indicated version!").build());
  }

  @Test
  void supports_tag8() {
    assertThat(validator.supports()).isEqualTo(8);
  }

  @ParameterizedTest
  @CsvSource({"D, true",
              "A, false"})
  void applicableToMessageType(String messageType, boolean isSupported) {
    assertThat(validator.applicableToMessageType(messageType))
        .isEqualTo(isSupported);
  }

}
