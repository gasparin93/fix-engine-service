package com.honestefforts.fixengine.service.validation.body;

import static com.honestefforts.fixengine.service.TestUtility.getRawTag;
import static org.assertj.core.api.Assertions.assertThat;

import com.honestefforts.fixengine.model.message.FixMessageContext;
import com.honestefforts.fixengine.model.validation.ValidationError;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class SideValidatorTest {

  SideValidator validator = new SideValidator();

  @ParameterizedTest
  @CsvSource({"1","2","3","4","5","6","7","8","9","A","B","C","D","E","F","G"})
  void validate_happyPath(String orderType) {
    ValidationError validationResult = validator.validate(
        getRawTag(54, orderType),
        FixMessageContext.builder()
            .messageType("D")
            .build());

    assertThat(validationResult.hasErrors()).isFalse();
  }

  @Test
  void validate_unsupportedOrderType_expectValidationError() {
    ValidationError validationResult = validator.validate(
        getRawTag(54, "ABCD"),
        FixMessageContext.builder()
            .messageType("D")
            .build());

    assertThat(validationResult).usingRecursiveComparison().withStrictTypeChecking()
        .isEqualTo(ValidationError.builder().submittedTag(getRawTag(54, "ABCD")).critical(true)
            .error("Provided Side (tag 54) is unsupported or invalid!").build());
  }

  @Test
  void supports_tag54() {
    assertThat(validator.supports()).isEqualTo(54);
  }

  @ParameterizedTest
  @CsvSource({"D, true",
              "A, false"})
  void applicableToMessageType(String messageType, boolean isSupported) {
    assertThat(validator.applicableToMessageType(messageType))
        .isEqualTo(isSupported);
  }

}
