package com.honestefforts.fixengine.service.converter.component;

import com.honestefforts.fixengine.model.message.components.FixTrailer;
import com.honestefforts.fixengine.model.message.tags.RawTag;
import java.util.Map;

public class FixTrailerConverter {
  public static FixTrailer convert(Map<String, RawTag> tagMap) {
    return FixTrailer.builder()
        .checkSum(tagMap.get("10").value())
        .signatureLength(Integer.parseInt(tagMap.get("93").value()))
        .signature(tagMap.get("89").value())
        .build();
  }
}
