package com.honestefforts.fixengine.service.converter.component;

import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseDouble;
import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseEnum;
import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseInt;

import com.honestefforts.fixengine.model.message.FixMessageContext;
import com.honestefforts.fixengine.model.message.components.SpreadOrBenchmarkCurveData;
import com.honestefforts.fixengine.model.universal.Currency;

public class SpreadOrBenchmarkCurveDataConverter {
  public static SpreadOrBenchmarkCurveData convert(FixMessageContext context) {
    return SpreadOrBenchmarkCurveData.builder()
        .spread(parseDouble(context.getValueForTag(218)))
        .benchmarkCurveCurrency(parseEnum(Currency.class, context.getValueForTag(220)))
        .benchmarkCurveName(context.getValueForTag(221))
        .benchmarkCurvePoint(context.getValueForTag(222))
        .benchmarkPrice(parseDouble(context.getValueForTag(662)))
        .benchmarkPriceType(parseInt(context.getValueForTag(663)))
        .benchmarkSecurityId(context.getValueForTag(699))
        .benchmarkSecurityIdSource(context.getValueForTag(761))
        .build();
  }
}
