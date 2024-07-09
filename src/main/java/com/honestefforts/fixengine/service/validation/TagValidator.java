package com.honestefforts.fixengine.service.validation;

import com.honestefforts.fixengine.model.message.tags.RawTag;
import com.honestefforts.fixengine.model.validation.ValidationError;
import com.honestefforts.fixengine.model.validation.Validator;
import jakarta.annotation.PostConstruct;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TagValidator {
  private final ApplicationContext applicationContext;
  private Map<String, Validator> validatorMap;
  private static final Set<String> supportedTags = IntStream.range(1, 957) //1-956
      .mapToObj(String::valueOf)
      .collect(Collectors.toSet());

  @PostConstruct
  public void init() {
    validatorMap = applicationContext.getBeansOfType(Validator.class)
        .values()
        .stream()
        .collect(Collectors.toMap(
            Validator::supports,
            validator -> validator
        ));
  }

  public ValidationError validateTag(RawTag rawTag, Map<String, RawTag> context) {
    return Optional.ofNullable(validatorMap.get(rawTag.tag()))
        .map(validator -> validator.validate(rawTag, context))
        .orElse(supportedTags.contains(rawTag.tag()) ? ValidationError.empty() :
            ValidationError.builder().submittedTag(rawTag).error("Unsupported tag").build());
  }

}
