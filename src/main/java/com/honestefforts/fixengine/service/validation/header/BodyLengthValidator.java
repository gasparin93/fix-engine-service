package com.honestefforts.fixengine.service.validation.header;

import static com.honestefforts.fixengine.model.message.enums.MessageType.NEW_ORDER_SINGLE;

import com.honestefforts.fixengine.model.message.FixMessageContext;
import com.honestefforts.fixengine.model.message.enums.MessageType;
import com.honestefforts.fixengine.model.message.tags.RawTag;
import com.honestefforts.fixengine.model.validation.FixValidator;
import com.honestefforts.fixengine.model.validation.ValidationError;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class BodyLengthValidator implements FixValidator {

  private static final Set<MessageType> applicableMessageTypes = Set.of(NEW_ORDER_SINGLE);

  @Override
  public ValidationError validate(final RawTag rawTag, final FixMessageContext context) {
    if(rawTag.position() != 2) {
      return ValidationError.builder().critical(true).submittedTag(rawTag)
          .error("BodyLength (9) tag must be the second tag in the message!").build();
    }
    return rawTag.errorIfNotCompliant(true);
  }

  @Override
  public Integer supports() {
    return 9;
  }

  @Override
  public boolean applicableToMessageType(MessageType messageType) {
    return applicableMessageTypes.contains(messageType);
  }

}
