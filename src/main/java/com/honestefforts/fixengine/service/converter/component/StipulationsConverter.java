package com.honestefforts.fixengine.service.converter.component;

import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseInt;

import com.honestefforts.fixengine.model.message.FixMessageContext;
import com.honestefforts.fixengine.model.message.components.Stipulations;
import com.honestefforts.fixengine.model.message.components.Stipulations.StipulationsBuilder;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

public class StipulationsConverter {
;
  private static final Map<Integer, BiConsumer<StipulationsBuilder, String>> tagMapping = Map.of(
      232, (builder, val) -> builder.numberOfStipulations(parseInt(val)),
      233, StipulationsBuilder::stipulationType,
      234, StipulationsBuilder::stipulationValue
  );

  public static Stipulations convert(FixMessageContext context) {
    StipulationsBuilder builder = Stipulations.builder();
    tagMapping.forEach((key, builderMapping) ->
        Optional.ofNullable(context.processedMessages().get(key))
            .ifPresent(rawTag -> builderMapping.accept(builder, rawTag.value())));
    return builder.build();
  }
}
