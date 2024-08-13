package com.honestefforts.fixengine.service.validation.body;

import static com.honestefforts.fixengine.model.message.enums.MessageType.NEW_ORDER_SINGLE;
import static com.honestefforts.fixengine.service.TestUtility.getContext;
import static com.honestefforts.fixengine.service.TestUtility.getRawTag;
import static org.assertj.core.api.Assertions.assertThat;

import com.honestefforts.fixengine.model.message.enums.MessageType;
import com.honestefforts.fixengine.model.validation.ValidationError;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class OrderTypeValidatorTest {

  OrderTypeValidator validator = new OrderTypeValidator();

  @ParameterizedTest
  @CsvSource({"1","2","3","4","6","7","8","9","D","E","G","I","J","K","L","M","P"})
  void validate_happyPath(String orderType) {
    ValidationError validationResult = validator.validate(
        getRawTag(40, orderType),
        getContext(NEW_ORDER_SINGLE));

    assertThat(validationResult.hasErrors()).isFalse();
  }

  @Test
  void validate_unsupportedOrderType_expectValidationError() {
    ValidationError validationResult = validator.validate(
        getRawTag(40, "ABCD"),
        getContext(NEW_ORDER_SINGLE));

    assertThat(validationResult).usingRecursiveComparison().withStrictTypeChecking()
        .isEqualTo(ValidationError.builder().submittedTag(getRawTag(40, "ABCD")).critical(true)
            .error("Provided Order Type (tag 40) is unsupported or invalid!").build());
  }

  @Test
  void supports_tag40() {
    assertThat(validator.supports()).isEqualTo(40);
  }

  @ParameterizedTest
  @CsvSource({"NEW_ORDER_SINGLE, true"})
  void applicableToMessageType(MessageType messageType, boolean isSupported) {
    assertThat(validator.applicableToMessageType(messageType)).isEqualTo(isSupported);
  }

}