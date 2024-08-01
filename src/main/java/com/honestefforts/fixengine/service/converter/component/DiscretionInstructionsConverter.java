package com.honestefforts.fixengine.service.converter.component;

import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseChar;
import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseDouble;
import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseInt;

import com.honestefforts.fixengine.model.message.FixMessageContext;
import com.honestefforts.fixengine.model.message.components.DiscretionInstructions.DiscretionInstructionsBuilder;
import com.honestefforts.fixengine.model.message.components.DiscretionInstructions;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

public class DiscretionInstructionsConverter {

  private static final Map<Integer, BiConsumer<DiscretionInstructionsBuilder, String>> tagMapping = Map.of(
      388, (builder, val) -> builder.discretionInstruction(parseChar(val)),
      389, (builder, val) -> builder.discretionOffsetValue(parseDouble(val)),
      841, (builder, val) -> builder.discretionMoveType(parseInt(val)),
      842, (builder, val) -> builder.discretionOffsetType(parseInt(val)),
      843, (builder, val) -> builder.discretionLimitType(parseInt(val)),
      844, (builder, val) -> builder.discretionRoundDirection(parseInt(val)),
      846, (builder, val) -> builder.discretionScope(parseInt(val))
  );

  public static DiscretionInstructions convert(FixMessageContext context) {
    DiscretionInstructionsBuilder builder = DiscretionInstructions.builder();
    tagMapping.forEach((key, builderMapping) ->
          Optional.ofNullable(context.processedMessages().get(key))
              .ifPresent(rawTag -> builderMapping.accept(builder, rawTag.value())));
      return builder.build();
  }

}
