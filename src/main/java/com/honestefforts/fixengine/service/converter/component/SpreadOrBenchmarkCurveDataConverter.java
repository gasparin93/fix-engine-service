package com.honestefforts.fixengine.service.converter.component;

import com.honestefforts.fixengine.model.message.components.SpreadOrBenchmarkCurveData;
import com.honestefforts.fixengine.model.message.tags.RawTag;
import com.honestefforts.fixengine.model.universal.Currency;
import java.util.Map;

public class SpreadOrBenchmarkCurveDataConverter {
  public static SpreadOrBenchmarkCurveData convert(Map<String, RawTag> tagMap) {
    return SpreadOrBenchmarkCurveData.builder()
        .spread(Double.parseDouble(tagMap.get("218").value()))
        .benchmarkCurveCurrency(Currency.valueOf(tagMap.get("220").value()))
        .benchmarkCurveName(tagMap.get("221").value())
        .benchmarkCurvePoint(tagMap.get("222").value())
        .benchmarkPrice(Double.parseDouble(tagMap.get("662").value()))
        .benchmarkPriceType(Integer.parseInt(tagMap.get("663").value()))
        .benchmarkSecurityId(tagMap.get("699").value())
        .benchmarkSecurityIdSource(tagMap.get("761").value())
        .build();
  }
}
