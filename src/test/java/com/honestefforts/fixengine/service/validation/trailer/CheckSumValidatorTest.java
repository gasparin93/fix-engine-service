package com.honestefforts.fixengine.service.validation.trailer;

import com.honestefforts.fixengine.model.message.FixMessageContext;
import com.honestefforts.fixengine.model.message.tags.RawTag;
import com.honestefforts.fixengine.model.validation.FixValidator;
import com.honestefforts.fixengine.model.validation.ValidationError;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class CheckSumValidatorTest implements FixValidator {

  private static final Set<String> applicableMessageTypes = Set.of("D");

  @Override
  public ValidationError validate(final RawTag rawTag, final FixMessageContext context) {
    if(rawTag.position() != context.messageLength()) {
      return ValidationError.builder().critical(true).submittedTag(rawTag)
          .error("CheckSum (10) tag must be the last tag in the message!").build();
    }
    return rawTag.errorIfNotCompliant(true);
  }

  @Override
  public Integer supports() {
    return 10;
  }

  @Override
  public boolean applicableToMessageType(String messageType) {
    return applicableMessageTypes.contains(messageType);
  }

}
