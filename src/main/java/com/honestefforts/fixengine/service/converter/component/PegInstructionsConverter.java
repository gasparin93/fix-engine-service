package com.honestefforts.fixengine.service.converter.component;

import com.honestefforts.fixengine.model.message.components.PegInstructions;
import com.honestefforts.fixengine.model.message.tags.RawTag;
import java.util.Map;

public class PegInstructionsConverter {
  public static PegInstructions convert(Map<String, RawTag> tagMap) {
    return PegInstructions.builder()
        .pegOffsetValue(Double.parseDouble(tagMap.get("211").value()))
        .pegMoveType(Integer.parseInt(tagMap.get("835").value()))
        .pegOffsetType(Integer.parseInt(tagMap.get("836").value()))
        .pegLimitType(Integer.parseInt(tagMap.get("837").value()))
        .pegRoundDirection(Integer.parseInt(tagMap.get("838").value()))
        .pegScope(Integer.parseInt(tagMap.get("840").value()))
        .build();
  }
}
