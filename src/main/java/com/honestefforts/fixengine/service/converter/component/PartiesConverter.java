package com.honestefforts.fixengine.service.converter.component;

import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseChar;
import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseInt;

import com.honestefforts.fixengine.model.message.FixMessageContext;
import com.honestefforts.fixengine.model.message.components.Parties;

public class PartiesConverter {
  public static Parties convert(FixMessageContext context) {
    return Parties.builder()
        .numberOfPartyIds(parseInt(context.getValueForTag(453)))
        .partyId(context.getValueForTag(448))
        .partyIdSource(parseChar(context.getValueForTag(447)))
        .partyRole(parseInt(context.getValueForTag(452)))
        .numberOfPartySubIds(parseInt(context.getValueForTag(802)))
        .partySubId(context.getValueForTag(523))
        .partySubIdType(parseInt(context.getValueForTag(803)))
        .build();
  }
}
