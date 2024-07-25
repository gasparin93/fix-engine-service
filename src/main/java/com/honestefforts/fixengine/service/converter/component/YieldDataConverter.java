package com.honestefforts.fixengine.service.converter.component;

import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseDate;

import com.honestefforts.fixengine.model.message.components.YieldData;
import com.honestefforts.fixengine.model.message.tags.RawTag;
import java.util.Map;

public class YieldDataConverter {
  public static YieldData convert(Map<String, RawTag> tagMap) {
    return YieldData.builder()
        .yieldType(tagMap.get("235").value())
        .yield(Double.parseDouble(tagMap.get("236").value()))
        .yieldCalcDate(parseDate(tagMap.get("701").value()))
        .yieldRedemptionDate(parseDate(tagMap.get("696").value()))
        .yieldRedemptionPrice(Double.parseDouble(tagMap.get("697").value()))
        .yieldRedemptionPriceType(Integer.parseInt(tagMap.get("698").value()))
        .build();
  }
}
