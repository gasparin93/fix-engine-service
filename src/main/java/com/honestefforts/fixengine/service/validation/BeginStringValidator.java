package com.honestefforts.fixengine.service.validation;

import ch.qos.logback.core.util.StringUtil;
import com.honestefforts.fixengine.model.message.tags.RawTag;
import com.honestefforts.fixengine.model.validation.ValidationError;
import com.honestefforts.fixengine.model.validation.Validator;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class BeginStringValidator implements Validator {

  //tag, isSupported
  private static final Map<String, Boolean> acceptedValues = Map.of(
      "4.0", false,
      "4.1", false,
      "4.2", false,
      "4.3", false,
      "4.4", true,
      "5.0", false
  );

  @Override
  public ValidationError validate(final RawTag rawTag, final Map<String, RawTag> context) {
    if(StringUtil.isNullOrEmpty(rawTag.value())) {
      return ValidationError.builder().critical(true).submittedTag(rawTag)
          .error(Validator.REQUIRED_ERROR_MSG).build();
    }
    if(!acceptedValues.containsKey(rawTag.value())) {
      return ValidationError.builder().critical(true).submittedTag(rawTag)
          .error("FIX version is not valid!").build();
    }
    if(!acceptedValues.get(rawTag.value())) {
      ValidationError.builder().critical(true).submittedTag(rawTag)
          .error("FIX version is not currently supported!").build();
    }
    if(!rawTag.version().equals(rawTag.value())) {
      return ValidationError.builder()
          .critical(true).submittedTag(rawTag)
          .error("FIX version in message is does not match the indicated version!").build();
    }
    return ValidationError.empty();
  }

  @Override
  public String supports() {
    return "87";
  }

  public static boolean isVersionSupported(String tag) {
    return Optional.ofNullable(acceptedValues.get(tag))
        .orElse(false);
  }

  public static boolean isVersionNotSupported(String tag) {
    return !isVersionSupported(tag);
  }

}
