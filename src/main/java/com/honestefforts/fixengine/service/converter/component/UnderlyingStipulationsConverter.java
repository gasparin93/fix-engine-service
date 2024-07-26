package com.honestefforts.fixengine.service.converter.component;

import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseInt;

import com.honestefforts.fixengine.model.message.FixMessageContext;
import com.honestefforts.fixengine.model.message.components.UnderlyingStipulations;

public class UnderlyingStipulationsConverter {
  public static UnderlyingStipulations convert(FixMessageContext context) {
    return UnderlyingStipulations.builder()
        .numberOfUnderlyingStipulations(parseInt(context.getValueForTag("887")))
        .underlyingStipulationType(context.getValueForTag("888"))
        .underlyingStipulationValue(context.getValueForTag("889"))
        .build();
  }
}
