package com.honestefforts.fixengine.service.validation;

import com.honestefforts.fixengine.model.message.tags.RawTag;
import com.honestefforts.fixengine.model.validation.FixValidator;
import com.honestefforts.fixengine.model.validation.ValidationError;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FixValidatorFactory {
  private static final Map<String, FixValidator> validatorMap = new HashMap<>();
  private static final Set<String> supportedTags = IntStream.range(1, 957) //1-956
      .filter(num -> num != 101 && num != 261 && num != 809)
      .mapToObj(String::valueOf)
      .collect(Collectors.toSet());

  @Autowired
  private FixValidatorFactory(List<FixValidator> validators) {
    validators.forEach(validator -> validatorMap.put(validator.supports(), validator));
  }

  public ValidationError validateTag(RawTag rawTag, Map<String, RawTag> context) {
    return Optional.ofNullable(validatorMap.get(rawTag.tag()))
        .map(fixValidator -> fixValidator.validate(rawTag, context))
        .orElse(isASupportedTag(rawTag) ? rawTag.errorIfNotCompliant(false) :
            ValidationError.builder().submittedTag(rawTag).error("Unsupported tag").build());
  }

  public static boolean isASupportedTag(RawTag rawTag) {
    return supportedTags.contains(rawTag.tag());
  }

}
