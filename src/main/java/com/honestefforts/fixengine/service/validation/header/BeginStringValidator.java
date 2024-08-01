package com.honestefforts.fixengine.service.validation.header;

import ch.qos.logback.core.util.StringUtil;
import com.honestefforts.fixengine.model.message.FixMessageContext;
import com.honestefforts.fixengine.model.message.tags.RawTag;
import com.honestefforts.fixengine.model.validation.FixValidator;
import com.honestefforts.fixengine.model.validation.ValidationError;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class BeginStringValidator implements FixValidator {

  private static final Set<String> applicableMessageTypes = Set.of("D");

  //tag, isSupported
  private static final Map<String, Boolean> acceptedValues = Map.of(
      "FIX.4.0", false,
      "FIX.4.1", false,
      "FIX.4.2", false,
      "FIX.4.3", false,
      "FIX.4.4", true,
      "FIX.5.0", false
  );

  @Override
  public ValidationError validate(final RawTag rawTag, final FixMessageContext context) {
    ValidationError.ValidationErrorBuilder validationErrorBuilder = ValidationError.builder()
        .critical(true).submittedTag(rawTag);
    if(StringUtil.isNullOrEmpty(rawTag.value())) {
      return validationErrorBuilder.error(FixValidator.REQUIRED_ERROR_MSG).build();
    }
    if(rawTag.position() != 1) {
      return validationErrorBuilder
          .error("BeginString (8) tag must be the first tag in the message!").build();
    }
    if(!acceptedValues.containsKey(rawTag.value())) {
      return validationErrorBuilder.error("FIX version is not valid!").build();
    }
    if(!acceptedValues.get(rawTag.value())) {
      validationErrorBuilder.error("FIX version is not currently supported!").build();
    }
    if(!rawTag.version().equals(rawTag.value())) {
      return validationErrorBuilder
          .error("FIX version in message does not match the indicated version!").build();
    }
    return ValidationError.empty();
  }

  @Override
  public Integer supports() {
    return 8;
  }

  @Override
  public boolean applicableToMessageType(String messageType) {
    return applicableMessageTypes.contains(messageType);
  }

  public static boolean isVersionSupported(String tag) {
    return Optional.ofNullable(acceptedValues.get(tag))
        .orElse(false);
  }

  public static boolean isVersionNotSupported(String tag) {
    return !isVersionSupported(tag);
  }

}
