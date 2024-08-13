package com.honestefforts.fixengine.service.validation;

import static com.honestefforts.fixengine.model.message.enums.MessageType.NEW_ORDER_SINGLE;
import static com.honestefforts.fixengine.service.TestUtility.getContext;
import static com.honestefforts.fixengine.service.TestUtility.getRawTagEntry;
import static org.assertj.core.api.Assertions.assertThat;

import com.honestefforts.fixengine.model.message.components.Instrument;
import com.honestefforts.fixengine.model.message.components.OrderQuantityData;
import com.honestefforts.fixengine.model.message.tags.RawTag;
import com.honestefforts.fixengine.model.validation.ValidationError;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class RequiredComponentValidationTest {

  @Test
  void validateRequiredComponentsForMessageType_happyPath() {
    List<ValidationError> validationErrors = RequiredComponentValidation
        .validateRequiredComponentsForMessageType(
            getContext(NEW_ORDER_SINGLE, someRequiredTagsForNewOrderSingleComponents()));
    assertThat(validationErrors).isEmpty();
  }

  @Test
  void validateRequiredComponentsForMessageType_emptyMap_expectValidationErrors() {
    List<ValidationError> validationErrors = RequiredComponentValidation
        .validateRequiredComponentsForMessageType(
            getContext(NEW_ORDER_SINGLE, Map.of()));
    assertThat(validationErrors).usingRecursiveFieldByFieldElementComparator()
        .containsExactlyInAnyOrder(
            ValidationError.builder().critical(true)
                .error("Message type D requires Instrument component! Requires at least one of: ["
                    + Instrument.getSupportedTags() + "]").build(),
            ValidationError.builder().critical(true)
                .error("Message type D requires OrderQuantityData component! Requires at least one of: ["
                    + OrderQuantityData.getSupportedTags() + "]").build()
    );
  }

  private Map<Integer, RawTag> someRequiredTagsForNewOrderSingleComponents() {
    return Map.ofEntries(
        getRawTagEntry(55, "text"), //Instrument
        getRawTagEntry(38, "text")  //OrderQuantityData
    );
  }

}
