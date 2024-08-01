package com.honestefforts.fixengine.service.converter.component;

import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseInt;

import com.honestefforts.fixengine.model.message.FixMessageContext;
import com.honestefforts.fixengine.model.message.components.UnderlyingStipulations;
import com.honestefforts.fixengine.model.message.components.UnderlyingStipulations.UnderlyingStipulationsBuilder;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

public class UnderlyingStipulationsConverter {

  private static final Map<Integer, BiConsumer<UnderlyingStipulationsBuilder, String>> tagMapping = Map.of(
      887, (builder, val) -> builder.numberOfUnderlyingStipulations(parseInt(val)),
      888, UnderlyingStipulationsBuilder::underlyingStipulationType,
      889, UnderlyingStipulationsBuilder::underlyingStipulationValue
  );

  public static UnderlyingStipulations convert(FixMessageContext context) {
    UnderlyingStipulationsBuilder builder = UnderlyingStipulations.builder();
    tagMapping.forEach((key, builderMapping) ->
        Optional.ofNullable(context.processedMessages().get(key))
            .ifPresent(rawTag -> builderMapping.accept(builder, rawTag.value())));
    return builder.build();
  }
}
