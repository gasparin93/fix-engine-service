package com.honestefforts.fixengine.service.converter.component;

import com.honestefforts.fixengine.model.message.components.UnderlyingStipulations;
import com.honestefforts.fixengine.model.message.tags.RawTag;
import java.util.Map;

public class UnderlyingStipulationsConverter {
  public static UnderlyingStipulations convert(Map<String, RawTag> tagMap) {
    return UnderlyingStipulations.builder()
        .numberOfUnderlyingStipulations(Integer.parseInt(tagMap.get("887").value()))
        .underlyingStipulationType(tagMap.get("888").value())
        .underlyingStipulationValue(tagMap.get("889").value())
        .build();
  }
}
