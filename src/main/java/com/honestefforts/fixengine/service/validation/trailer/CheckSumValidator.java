package com.honestefforts.fixengine.service.validation.trailer;

import com.honestefforts.fixengine.model.message.FixMessageContext;
import com.honestefforts.fixengine.model.message.tags.RawTag;
import com.honestefforts.fixengine.model.validation.FixValidator;
import com.honestefforts.fixengine.model.validation.ValidationError;
import org.springframework.stereotype.Component;

@Component
public class CheckSumValidator implements FixValidator {

  @Override
  public ValidationError validate(final RawTag rawTag, final FixMessageContext context) {
    if(rawTag.position() != context.messageLength()) {
      return ValidationError.builder().critical(true).submittedTag(rawTag)
          .error("CheckSum (10) tag must be the last tag in the message!").build();
    }
    return rawTag.errorIfNotCompliant(true);
  }

  @Override
  public String supports() {
    return "10";
  }

}
