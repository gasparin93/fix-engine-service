package com.honestefforts.fixengine.service.converter.component;

import com.honestefforts.fixengine.model.message.components.Stipulations;
import com.honestefforts.fixengine.model.message.tags.RawTag;
import java.util.Map;

public class StipulationsConverter {
  public static Stipulations convert(Map<String, RawTag> tagMap) {
    return Stipulations.builder()
        .numberOfStipulations(Integer.parseInt(tagMap.get("232").value()))
        .stipulationType(tagMap.get("233").value())
        .stipulationValue(tagMap.get("234").value())
        .build();
  }
}
