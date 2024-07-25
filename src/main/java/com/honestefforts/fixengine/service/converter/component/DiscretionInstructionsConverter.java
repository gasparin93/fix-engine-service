package com.honestefforts.fixengine.service.converter.component;

import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseChar;

import com.honestefforts.fixengine.model.message.components.DiscretionInstructions;
import com.honestefforts.fixengine.model.message.tags.RawTag;
import java.util.Map;

public class DiscretionInstructionsConverter {
  public static DiscretionInstructions convert(Map<String, RawTag> tagMap) {
    return DiscretionInstructions.builder()
        .discretionInstruction(parseChar(tagMap.get("388").value()))
        .discretionOffsetValue(Double.parseDouble(tagMap.get("389").value()))
        .discretionMoveType(Integer.parseInt(tagMap.get("841").value()))
        .discretionOffsetType(Integer.parseInt(tagMap.get("842").value()))
        .discretionLimitType(Integer.parseInt(tagMap.get("843").value()))
        .discretionRoundDirection(Integer.parseInt(tagMap.get("844").value()))
        .discretionScope(Integer.parseInt(tagMap.get("846").value()))
        .build();
  }
}
