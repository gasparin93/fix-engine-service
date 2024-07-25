package com.honestefforts.fixengine.service.validation;

import com.honestefforts.fixengine.model.message.FixMessageContext;
import com.honestefforts.fixengine.model.message.components.Instrument;
import com.honestefforts.fixengine.model.message.components.OrderQuantityData;
import com.honestefforts.fixengine.model.validation.ValidationError;
import java.util.List;
import java.util.Map;

/**
 * This utility class is to validate that required components are not empty.
 * MessageHeader and MessageTrailer are available to all, so will not be included here.
 */
public class RequiredComponentValidation {

  private static final Map<String, List<Component>> messageTypeToRequiredComponents = Map.of(
      "D", List.of(
          new Component("Instrument", Instrument.supportedTags()),
          new Component("OrderQuantityData", OrderQuantityData.supportedTags())
      )
  );

  public static List<ValidationError> validateRequiredComponentsForMessageType(
      FixMessageContext context) {
    if(!messageTypeToRequiredComponents.containsKey(context.messageType())) {
      return List.of(); //no required components for message type
    }
    return messageTypeToRequiredComponents.get(context.messageType()).stream()
        .filter(requiredComponent -> requiredComponent.componentTags().stream()
            .anyMatch(context::hasTag))
        .map(requiredComponent -> ValidationError.builder().critical(true)
            .error("Message type " + context.messageType() + " requires "
                + requiredComponent.componentName() + " component! Requires at least one of: ["
                + requiredComponent.componentTags() + "]").build()
        )
        .toList();
  }

  private record Component(String componentName, List<String> componentTags) {}

}
