package com.honestefforts.fixengine.service.validation;

import static com.honestefforts.fixengine.model.validation.FixValidator.EMPTY_OR_NULL_VALUE;
import static com.honestefforts.fixengine.model.validation.FixValidator.REQUIRED_ERROR_MSG;

import com.honestefforts.fixengine.model.message.FixMessageContext;
import com.honestefforts.fixengine.model.message.tags.RawTag;
import com.honestefforts.fixengine.model.validation.FixValidator;
import com.honestefforts.fixengine.model.validation.ValidationError;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FixValidatorFactory {
  private final Map<Integer, FixValidator> validatorMap;
  private static final Map<String, Set<Integer>> requiredGenericValidationTagsByMsgType = Map.of(
      "D", Set.of(34, 49, 52, 56, 60)
  );
  private static final Set<Integer> supportedTags = IntStream.range(1, 957) //1-956
      .filter(num -> num != 101 && num != 261 && num != 809)
      .boxed()
      .collect(Collectors.toSet());

  @Autowired
  private FixValidatorFactory(List<FixValidator> validators) {
    validatorMap = validators.stream()
        .collect(Collectors.toMap(FixValidator::supports, Function.identity()));
  }

  public ValidationError validateTag(RawTag rawTag, FixMessageContext context) {
    return Optional.ofNullable(validatorMap.get(rawTag.tag()))
        .filter(fixValidator -> fixValidator.applicableToMessageType(context.messageType()))
        .map(fixValidator -> fixValidator.validate(rawTag, context))
        .orElse(isASupportedTag(rawTag) ?
           doGenericValidation(rawTag, context.messageType())
            : ValidationError.builder().submittedTag(rawTag).error("Unsupported tag").build());
  }

  public static boolean isASupportedTag(RawTag rawTag) {
    return supportedTags.contains(rawTag.tag());
  }

  private ValidationError doGenericValidation(RawTag rawTag, String messageType) {
    boolean isCritical = Optional.ofNullable(requiredGenericValidationTagsByMsgType.get(messageType))
        .map(messageTypeRequiredTags -> messageTypeRequiredTags.contains(rawTag.tag()))
        .orElse(false);
    return Optional.ofNullable(rawTag.value())
        .filter(val -> !val.isBlank())
        .map(_ -> rawTag.errorIfNotCompliant(isCritical))
        .orElse(ValidationError.builder().critical(isCritical).submittedTag(rawTag)
            .error(isCritical ? REQUIRED_ERROR_MSG : EMPTY_OR_NULL_VALUE).build());
  }

}
