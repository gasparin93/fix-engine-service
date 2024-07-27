package com.honestefforts.fixengine.service.converter.component;

import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseChar;
import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseInt;

import com.honestefforts.fixengine.model.message.FixMessageContext;
import com.honestefforts.fixengine.model.message.components.NestedParties;

public class NestedPartiesConverter {
  public static NestedParties convert(FixMessageContext context) {
    return NestedParties.builder()
        .numberOfNestedPartyIds(parseInt(context.getValueForTag(539)))
        .nestedPartyId(context.getValueForTag(524))
        .nestedPartyIdSource(parseChar(context.getValueForTag(525)))
        .nestedPartyRole(parseInt(context.getValueForTag(538)))
        .numberOfNestedPartySubIds(parseInt(context.getValueForTag(804)))
        .nestedPartySubId(context.getValueForTag(545))
        .nestedPartySubIdType(parseInt(context.getValueForTag(805)))
        .build();
  }
}
