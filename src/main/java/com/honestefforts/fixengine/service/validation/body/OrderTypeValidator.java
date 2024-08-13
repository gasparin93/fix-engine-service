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
public class OrderTypeValidator implements FixValidator {

  private static final Set<MessageType> applicableMessageTypes = Set.of(NEW_ORDER_SINGLE);

  //TODO: these should be an enum
  private static final Set<String> acceptedValues = Set.of(
      "1", //Market
      "2", //Limit
      "3", //Stop
      "4", //Stop limit
      "6", //With or without
      "7", //Limit or better (Deprecated)
      "8", //Limit with or without
      "9", //On basis
      "D", //Previously quoted
      "E", //Previously indicated
      "G", //Forex - Swap
      "I", //Funari (Limit Day Order with unexecuted portion handled as Market On Close. E.g. Japan)
      "J", //Market If Touched (MIT)
      "K", //Market with Leftover as Limit (market order then unexecuted quantity becomes limit order at last price)
      "L", //Previous Fund Valuation Point (Historic pricing) (for CIV)
      "M", //Next Fund Valuation Point (Forward pricing) (for CIV)
      "P" //Pegged
  );

  @Override
  public ValidationError validate(RawTag rawTag, FixMessageContext context) {
    return acceptedValues.contains(rawTag.value()) ? ValidationError.empty()
        : ValidationError.builder().submittedTag(rawTag).critical(true)
            .error("Provided Order Type (tag 40) is unsupported or invalid!").build();
  }
  
  @Override
  public Integer supports() {
    return 40;
  }

  @Override
  public boolean applicableToMessageType(MessageType messageType) {
    return applicableMessageTypes.contains(messageType);
  }

}