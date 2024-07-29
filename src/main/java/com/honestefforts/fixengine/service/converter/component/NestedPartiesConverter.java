package com.honestefforts.fixengine.service.converter.component;

import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseChar;
import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseInt;

import com.honestefforts.fixengine.model.message.FixMessageContext;
import com.honestefforts.fixengine.model.message.components.NestedParties;
import com.honestefforts.fixengine.model.message.components.NestedParties.NestedPartiesBuilder;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

public class NestedPartiesConverter {

  private static final Map<Integer, BiConsumer<NestedPartiesBuilder, String>> tagMapping = Map.of(
      524, NestedPartiesBuilder::nestedPartyId,
      525, (builder, val) -> builder.nestedPartyIdSource(parseChar(val)),
      538, (builder, val) -> builder.nestedPartyRole(parseInt(val)),
      539, (builder, val) -> builder.numberOfNestedPartyIds(parseInt(val)),
      545, NestedPartiesBuilder::nestedPartySubId,
      804, (builder, val) -> builder.numberOfNestedPartySubIds(parseInt(val)),
      805, (builder, val) -> builder.nestedPartySubIdType(parseInt(val))
  );

  public static NestedParties convert(FixMessageContext context) {
    NestedPartiesBuilder builder = NestedParties.builder();
    tagMapping.forEach((key, builderMapping) ->
        Optional.ofNullable(context.processedMessages().get(key))
            .ifPresent(rawTag -> builderMapping.accept(builder, rawTag.value())));
    return builder.build();
  }
}
