package com.honestefforts.fixengine.service.converter.component;

import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseChar;

import com.honestefforts.fixengine.model.message.components.OrderQuantityData;
import com.honestefforts.fixengine.model.message.tags.RawTag;
import java.util.Map;

public class OrderQuantityDataConverter {
  public static OrderQuantityData convert(Map<String, RawTag> tagMap) {
    return OrderQuantityData.builder()
        .orderQuantity(Integer.parseInt(tagMap.get("38").value()))
        .cashOrderQuantity(Integer.parseInt(tagMap.get("152").value()))
        .orderPercent(Double.parseDouble(tagMap.get("516").value()))
        .roundingDirection(parseChar(tagMap.get("468").value()))
        .roundingModulus(Double.parseDouble(tagMap.get("469").value()))
        .build();
  }
}
