package com.honestefforts.fixengine.service.validation;

import com.honestefforts.fixengine.model.message.tags.RawTag;
import java.util.List;
import java.util.Map;

public class FixHeaderValidator implements Validator {

  //tag, isSupported
  private static final List<String> requiredFields = List.of(
  );

  @Override
  public ValidationError validate(RawTag rawTag, Map<String, RawTag> context) {
    return validate(context);
  }

  public static ValidationError validate(Map<String, RawTag> context) {
    List<String> missing = requiredFields.stream()
        .filter(required -> !context.containsKey(required))
        .toList();
    return missing.isEmpty() ?
        ValidationError.empty()
        : ValidationError.builder().critical(true)
            .error("Missing required header values: "+missing).build();
  }

}
