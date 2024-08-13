package com.honestefforts.fixengine.service.validation.trailer;

import static com.honestefforts.fixengine.model.message.enums.MessageType.NEW_ORDER_SINGLE;
import static com.honestefforts.fixengine.model.message.tags.TagType.STRING;
import static org.assertj.core.api.Assertions.assertThat;

import com.honestefforts.fixengine.model.message.FixMessageContext;
import com.honestefforts.fixengine.model.message.enums.MessageType;
import com.honestefforts.fixengine.model.message.tags.RawTag;
import com.honestefforts.fixengine.model.validation.ValidationError;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class CheckSumValidatorTest {

  CheckSumValidator validator = new CheckSumValidator();

  @Test
  void validate_happyPath() {
    ValidationError validationResult = validator.validate(
        RawTag.builder().tag(10).dataType(STRING).value("checksum").position(50).build(),
        FixMessageContext.builder()
            .processedMessages(Map.of())
            .version("FIX.4.4")
            .messageType(NEW_ORDER_SINGLE)
            .messageLength(50)
            .build());

    assertThat(validationResult.hasErrors()).isFalse();
  }

  @Test
  void validate_notLastInMessage_expectValidationError() {
    RawTag tag = RawTag.builder().tag(10).dataType(STRING).value("checksum").position(49).build();
    ValidationError validationResult = validator.validate(tag,
        FixMessageContext.builder()
            .processedMessages(Map.of())
            .version("FIX.4.4")
            .messageType(NEW_ORDER_SINGLE)
            .messageLength(50)
            .build());

    assertThat(validationResult).usingRecursiveComparison().withStrictTypeChecking()
        .isEqualTo(ValidationError.builder().submittedTag(tag).critical(true)
            .error("CheckSum (10) tag must be the last tag in the message!").build());
  }

  @Test
  void supports_tag10() {
    assertThat(validator.supports()).isEqualTo(10);
  }

  @ParameterizedTest
  @CsvSource({"NEW_ORDER_SINGLE, true"})
  void applicableToMessageType(MessageType messageType, boolean isSupported) {
    assertThat(validator.applicableToMessageType(messageType)).isEqualTo(isSupported);
  }

}
