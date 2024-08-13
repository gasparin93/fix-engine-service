package com.honestefforts.fixengine.service.validation.trailer;

import static com.honestefforts.fixengine.model.message.enums.MessageType.NEW_ORDER_SINGLE;

import com.honestefforts.fixengine.model.message.FixMessageContext;
import com.honestefforts.fixengine.model.message.enums.MessageType;
import com.honestefforts.fixengine.model.message.tags.RawTag;
import com.honestefforts.fixengine.model.validation.FixValidator;
import com.honestefforts.fixengine.model.validation.ValidationError;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class CheckSumValidator implements FixValidator {

  private static final Set<MessageType> applicableMessageTypes = Set.of(NEW_ORDER_SINGLE);

  @Override
  public ValidationError validate(final RawTag rawTag, final FixMessageContext context) {
    if(rawTag.position() != context.messageLength()) { //null is filtered out on factory
      return ValidationError.builder().critical(true).submittedTag(rawTag)
          .error("CheckSum (10) tag must be the last tag in the message!").build();
    }
    return ValidationError.empty();
  }

  @Override
  public Integer supports() {
    return 10;
  }

  @Override
  public boolean applicableToMessageType(MessageType messageType) {
    return applicableMessageTypes.contains(messageType);
  }

}
