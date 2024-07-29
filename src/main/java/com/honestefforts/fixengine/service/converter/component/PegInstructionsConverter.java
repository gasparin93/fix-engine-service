package com.honestefforts.fixengine.service.converter.component;

import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseDouble;
import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseInt;

import com.honestefforts.fixengine.model.message.FixMessageContext;
import com.honestefforts.fixengine.model.message.components.PegInstructions;
import com.honestefforts.fixengine.model.message.components.PegInstructions.PegInstructionsBuilder;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

public class PegInstructionsConverter {

  private static final Map<Integer, BiConsumer<PegInstructionsBuilder, String>> tagMapping = Map.of(
      211, (builder, val) -> builder.pegOffsetValue(parseDouble(val)),
      835, (builder, val) -> builder.pegMoveType(parseInt(val)),
      836, (builder, val) -> builder.pegOffsetType(parseInt(val)),
      837, (builder, val) -> builder.pegLimitType(parseInt(val)),
      838, (builder, val) -> builder.pegRoundDirection(parseInt(val)),
      840, (builder, val) -> builder.pegScope(parseInt(val))
  );

  public static PegInstructions convert(FixMessageContext context) {
    PegInstructionsBuilder builder = PegInstructions.builder();
    tagMapping.forEach((key, builderMapping) ->
        Optional.ofNullable(context.processedMessages().get(key))
            .ifPresent(rawTag -> builderMapping.accept(builder, rawTag.value())));
    return builder.build();
  }
}
