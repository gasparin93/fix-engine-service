package com.honestefforts.fixengine.service.converter.component;

import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseChar;
import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseInt;

import com.honestefforts.fixengine.model.message.FixMessageContext;
import com.honestefforts.fixengine.model.message.components.Parties;
import com.honestefforts.fixengine.model.message.components.Parties.PartiesBuilder;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

public class PartiesConverter {

  private static final Map<Integer, BiConsumer<PartiesBuilder, String>> tagMapping = Map.of(
      447, (builder, val) -> builder.partyIdSource(parseChar(val)),
      448, PartiesBuilder::partyId,
      452, (builder, val) -> builder.partyRole(parseInt(val)),
      453, (builder, val) -> builder.numberOfPartyIds(parseInt(val)),
      523, PartiesBuilder::partySubId,
      802, (builder, val) -> builder.numberOfPartySubIds(parseInt(val)),
      803, (builder, val) -> builder.partySubIdType(parseInt(val))
  );

  public static Parties convert(FixMessageContext context) {
    PartiesBuilder builder = Parties.builder();
    tagMapping.forEach((key, builderMapping) ->
        Optional.ofNullable(context.processedMessages().get(key))
            .ifPresent(rawTag -> builderMapping.accept(builder, rawTag.value())));
    return builder.build();
  }
}
