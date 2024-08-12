package com.honestefforts.fixengine.service.validation.header;

import static com.honestefforts.fixengine.model.message.tags.TagType.STRING;
import static com.honestefforts.fixengine.service.TestUtility.getContext;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.honestefforts.fixengine.model.converter.FixConverter;
import com.honestefforts.fixengine.model.message.NewOrderSingle;
import com.honestefforts.fixengine.model.message.tags.RawTag;
import com.honestefforts.fixengine.model.validation.ValidationError;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MessageTypeValidatorTest {

  @Mock
  FixConverter<NewOrderSingle> converter;

  MessageTypeValidator validator;

  @BeforeEach
  void setUp() {
    when(converter.supports()).thenReturn("D");
    validator = new MessageTypeValidator(List.of(converter));
  }

  @ParameterizedTest
  @CsvSource({"D"})
  void validate_happyPath(String type) {
    ValidationError validationResult = validator.validate(
        RawTag.builder().tag(35).dataType(STRING).value(type).position(3).build(),
        getContext("D"));

    assertThat(validationResult).usingRecursiveComparison().withStrictTypeChecking().isEqualTo(ValidationError.empty());
    assertThat(validationResult.hasErrors()).isFalse();
  }

  @Test
  void validate_notThirdInMessage_expectValidationError() {
    RawTag tag = RawTag.builder().tag(35).dataType(STRING).value("D").position(2).build();
    ValidationError validationResult = validator.validate(tag, getContext("D"));

    assertThat(validationResult).usingRecursiveComparison().withStrictTypeChecking()
        .isEqualTo(ValidationError.builder().submittedTag(tag).critical(true)
            .error("MsgType (35) tag must be the third tag in the message!").build());
  }

  @Test
  void validate_invalidType_expectValidationError() {
    RawTag tag = RawTag.builder().tag(35).dataType(STRING).value("ABCD").position(3).build();
    ValidationError validationResult = validator.validate(tag, getContext("D"));

    assertThat(validationResult).usingRecursiveComparison().withStrictTypeChecking()
        .isEqualTo(ValidationError.builder().submittedTag(tag).critical(true)
            .error("Message type is invalid!").build());
  }

  @Test
  void validate_customType_expectValidationError() {
    RawTag tag = RawTag.builder().tag(35).dataType(STRING).value("UXX").position(3).build();
    ValidationError validationResult = validator.validate(tag, getContext("D"));

    assertThat(validationResult).usingRecursiveComparison().withStrictTypeChecking()
        .isEqualTo(ValidationError.builder().submittedTag(tag).critical(true)
            .error("Unknown private message format!").build());
  }

  @Test
  void validate_unsupportedType_expectValidationError() {
    RawTag tag = RawTag.builder().tag(8).dataType(STRING).value("BH").position(3).build();
    ValidationError validationResult = validator.validate(tag, getContext("D"));

    assertThat(validationResult).usingRecursiveComparison().withStrictTypeChecking()
        .isEqualTo(ValidationError.builder().submittedTag(tag).critical(true)
            .error("Message Type is not currently supported!").build());
  }

  @Test
  void supports_tag35() {
    assertThat(validator.supports()).isEqualTo(35);
  }

  @ParameterizedTest
  @CsvSource({"D, true",
              "A, false"})
  void applicableToMessageType(String messageType, boolean isSupported) {
    assertThat(validator.applicableToMessageType(messageType))
        .isEqualTo(isSupported);
  }

}
