package com.honestefforts.fixengine.service.validation.header;

import com.honestefforts.fixengine.model.message.tags.RawTag;
import com.honestefforts.fixengine.model.validation.FixValidator;
import com.honestefforts.fixengine.model.validation.ValidationError;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class BodyLengthValidator implements FixValidator {

  @Override
  public ValidationError validate(final RawTag rawTag, final Map<String, RawTag> context) {
    if(rawTag.position() != 2) {
      return ValidationError.builder().critical(true).submittedTag(rawTag)
          .error("BodyLength (9) tag must be the second tag in the message!").build();
    }
    return rawTag.errorIfNotCompliant(true);
  }

  @Override
  public String supports() {
    return "9";
  }

}
