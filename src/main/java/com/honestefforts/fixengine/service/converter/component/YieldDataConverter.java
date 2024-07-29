package com.honestefforts.fixengine.service.converter.component;

import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseDate;
import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseDouble;
import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseInt;

import com.honestefforts.fixengine.model.message.FixMessageContext;
import com.honestefforts.fixengine.model.message.components.YieldData;
import com.honestefforts.fixengine.model.message.components.YieldData.YieldDataBuilder;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

public class YieldDataConverter {
  private static final Map<Integer, BiConsumer<YieldDataBuilder, String>> tagMapping = Map.of(
      235, YieldDataBuilder::yieldType,
      236, (builder, val) -> builder.yield(parseDouble(val)),
      696, (builder, val) -> builder.yieldRedemptionDate(parseDate(val)),
      697, (builder, val) -> builder.yieldRedemptionPrice(parseDouble(val)),
      698, (builder, val) -> builder.yieldRedemptionPriceType(parseInt(val)),
      701, (builder, val) -> builder.yieldCalcDate(parseDate(val))
      );

  public static YieldData convert(FixMessageContext context) {
    YieldDataBuilder builder = YieldData.builder();
    tagMapping.forEach((key, builderMapping) ->
        Optional.ofNullable(context.processedMessages().get(key))
            .ifPresent(rawTag -> builderMapping.accept(builder, rawTag.value())));
    return builder.build();
  }
}
