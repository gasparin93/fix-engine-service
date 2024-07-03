package com.honestefforts.fixengine.service.validation;

import com.honestefforts.fixengine.model.message.tags.RawTag;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TagValidator {
  private static final Map<String, Validator> tagValidations = Map.of(
      "35", new MessageTypeValidator()
  );

  private static final Set<String> supportedTags = IntStream.range(1, 957) //1-956
      .mapToObj(String::valueOf)
      .collect(Collectors.toSet());

  public static ValidationError validateTag(RawTag rawTag, Map<String, RawTag> context) {
    return Optional.ofNullable(tagValidations.get(rawTag.tag()))
        .map(validator -> validator.validate(rawTag, context))
        .orElse(supportedTags.contains(rawTag.tag()) ? ValidationError.empty() :
            ValidationError.builder().submittedTag(rawTag).error("Unsupported tag").build());
  }

}
