package com.honestefforts.fixengine.service.converter.component;

import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseChar;
import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseDouble;
import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseInt;

import com.honestefforts.fixengine.model.message.FixMessageContext;
import com.honestefforts.fixengine.model.message.components.OrderQuantityData;
import com.honestefforts.fixengine.model.message.components.OrderQuantityData.OrderQuantityDataBuilder;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

public class OrderQuantityDataConverter {

  private static final Map<Integer, BiConsumer<OrderQuantityDataBuilder, String>> tagMapping = Map.of(
      38, (builder, val) -> builder.orderQuantity(parseInt(val)),
      152, (builder, val) -> builder.cashOrderQuantity(parseInt(val)),
      468, (builder, val) -> builder.roundingDirection(parseChar(val)),
      469, (builder, val) -> builder.roundingModulus(parseDouble(val)),
      516, (builder, val) -> builder.orderPercent(parseDouble(val))
  );

  public static OrderQuantityData convert(FixMessageContext context) {
    OrderQuantityDataBuilder builder = OrderQuantityData.builder();
    tagMapping.forEach((key, builderMapping) ->
        Optional.ofNullable(context.processedMessages().get(key))
            .ifPresent(rawTag -> builderMapping.accept(builder, rawTag.value())));
    return builder.build();
  }
}
