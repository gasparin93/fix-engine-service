package com.honestefforts.fixengine.service.converter.component;

import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseDouble;
import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseInt;

import com.honestefforts.fixengine.model.message.FixMessageContext;
import com.honestefforts.fixengine.model.message.components.PegInstructions;

public class PegInstructionsConverter {
  public static PegInstructions convert(FixMessageContext context) {
    return PegInstructions.builder()
        .pegOffsetValue(parseDouble(context.getValueForTag("211")))
        .pegMoveType(parseInt(context.getValueForTag("835")))
        .pegOffsetType(parseInt(context.getValueForTag("836")))
        .pegLimitType(parseInt(context.getValueForTag("837")))
        .pegRoundDirection(parseInt(context.getValueForTag("838")))
        .pegScope(parseInt(context.getValueForTag("840")))
        .build();
  }
}
