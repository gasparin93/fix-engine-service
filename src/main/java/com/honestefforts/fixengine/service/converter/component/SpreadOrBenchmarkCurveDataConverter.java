package com.honestefforts.fixengine.service.converter.component;

import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseDouble;
import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseEnum;
import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseInt;

import com.honestefforts.fixengine.model.message.FixMessageContext;
import com.honestefforts.fixengine.model.message.components.SpreadOrBenchmarkCurveData;
import com.honestefforts.fixengine.model.message.components.SpreadOrBenchmarkCurveData.SpreadOrBenchmarkCurveDataBuilder;
import com.honestefforts.fixengine.model.universal.Currency;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

public class SpreadOrBenchmarkCurveDataConverter {

  private static final Map<Integer, BiConsumer<SpreadOrBenchmarkCurveDataBuilder, String>> tagMapping = Map.of(
      218, (builder, val) -> builder.spread(parseDouble(val)),
      220, (builder, val) -> builder.benchmarkCurveCurrency(parseEnum(Currency.class, val)),
      221, SpreadOrBenchmarkCurveDataBuilder::benchmarkCurveName,
      222, SpreadOrBenchmarkCurveDataBuilder::benchmarkCurvePoint,
      662, (builder, val) -> builder.benchmarkPrice(parseDouble(val)),
      663, (builder, val) -> builder.benchmarkPriceType(parseInt(val)),
      699, SpreadOrBenchmarkCurveDataBuilder::benchmarkSecurityId,
      761, SpreadOrBenchmarkCurveDataBuilder::benchmarkSecurityIdSource
  );

  public static SpreadOrBenchmarkCurveData convert(FixMessageContext context) {
    SpreadOrBenchmarkCurveDataBuilder builder = SpreadOrBenchmarkCurveData.builder();
    tagMapping.forEach((key, builderMapping) ->
        Optional.ofNullable(context.processedMessages().get(key))
            .ifPresent(rawTag -> builderMapping.accept(builder, rawTag.value())));
    return builder.build();
  }
}
