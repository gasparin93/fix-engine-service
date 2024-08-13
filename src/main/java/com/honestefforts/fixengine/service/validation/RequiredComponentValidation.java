package com.honestefforts.fixengine.service.validation;

import com.honestefforts.fixengine.model.message.FixMessageContext;
import com.honestefforts.fixengine.model.validation.ValidationError;
import java.util.List;

/**
 * This utility class is to validate that required components are not empty.
 * MessageHeader and MessageTrailer are required by all and have required tags, no need to include.
 */
public class RequiredComponentValidation {

  public static List<ValidationError> validateRequiredComponentsForMessageType(
      FixMessageContext context) {
    return context.messageType().getRequiredComponents().stream()
        .filter(requiredComponent -> requiredComponent.supportedTags().stream()
            .noneMatch(context::hasTag))
        .map(requiredComponent -> ValidationError.builder().critical(true)
            .error("Message type " + context.messageType().getFixSymbol() + " requires "
                + requiredComponent.component() + " component! Requires at least one of: ["
                + requiredComponent.supportedTags() + "]").build()
        )
        .toList();
  }

}
