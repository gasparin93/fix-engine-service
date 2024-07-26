package com.honestefforts.fixengine.service.converter.component;

import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseInt;

import com.honestefforts.fixengine.model.message.FixMessageContext;
import com.honestefforts.fixengine.model.message.components.Stipulations;

public class StipulationsConverter {
  public static Stipulations convert(FixMessageContext context) {
    return Stipulations.builder()
        .numberOfStipulations(parseInt(context.getValueForTag("232")))
        .stipulationType(context.getValueForTag("233"))
        .stipulationValue(context.getValueForTag("234"))
        .build();
  }
}
