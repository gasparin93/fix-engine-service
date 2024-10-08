package com.honestefforts.fixengine.service.validation.body;

import static com.honestefforts.fixengine.model.message.enums.MessageType.NEW_ORDER_SINGLE;

import com.honestefforts.fixengine.model.message.FixMessageContext;
import com.honestefforts.fixengine.model.message.enums.MessageType;
import com.honestefforts.fixengine.model.message.tags.RawTag;
import com.honestefforts.fixengine.model.validation.FixValidator;
import com.honestefforts.fixengine.model.validation.ValidationError;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class SideValidator implements FixValidator {

  private static final Set<MessageType> applicableMessageTypes = Set.of(NEW_ORDER_SINGLE);

  //TODO: these should be an enum
  private static final Set<String> acceptedValues = Set.of(
      "1", //Buy
      "2", //Sell
      "3", //Buy minus
      "4", //Sell plus
      "5", //Sell short
      "6", //Sell short exempt
      "7", //Undisclosed (valid for IOI and List Order messages only)
      "8", //Cross (orders where counterparty is an exchange, valid for all messages except IOIs)
      "9", //Cross short
      "A", //Cross short exempt
      "B", //"As Defined" (for use with multileg instruments)
      "C", //"Opposite" (for use with multileg instruments)
      "D", //Subscribe (e.g. CIV)
      "E", //Redeem (e.g. CIV)
      "F", //Lend (FINANCING - identifies direction of collateral)
      "G" //Borrow (FINANCING - identifies direction of collateral)
  );

  @Override
  public ValidationError validate(RawTag rawTag, FixMessageContext context) {
    return acceptedValues.contains(rawTag.value()) ? ValidationError.empty()
        : ValidationError.builder().submittedTag(rawTag).critical(true)
            .error("Provided Side (tag 54) is unsupported or invalid!").build();
  }
  
  @Override
  public Integer supports() {
    return 54;
  }

  @Override
  public boolean applicableToMessageType(MessageType messageType) {
    return applicableMessageTypes.contains(messageType);
  }

}
