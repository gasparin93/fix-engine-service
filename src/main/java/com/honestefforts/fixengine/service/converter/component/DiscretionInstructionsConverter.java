package com.honestefforts.fixengine.service.converter.component;

import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseChar;
import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseDouble;
import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseInt;

import com.honestefforts.fixengine.model.message.FixMessageContext;
import com.honestefforts.fixengine.model.message.components.DiscretionInstructions;

public class DiscretionInstructionsConverter {
  public static DiscretionInstructions convert(FixMessageContext context) {
    return DiscretionInstructions.builder()
        .discretionInstruction(parseChar(context.getValueForTag("388")))
        .discretionOffsetValue(parseDouble(context.getValueForTag("389")))
        .discretionMoveType(parseInt(context.getValueForTag("841")))
        .discretionOffsetType(parseInt(context.getValueForTag("842")))
        .discretionLimitType(parseInt(context.getValueForTag("843")))
        .discretionRoundDirection(parseInt(context.getValueForTag("844")))
        .discretionScope(parseInt(context.getValueForTag("846")))
        .build();
  }
}
