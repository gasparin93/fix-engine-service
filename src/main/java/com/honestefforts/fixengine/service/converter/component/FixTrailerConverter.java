package com.honestefforts.fixengine.service.converter.component;

import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseInt;

import com.honestefforts.fixengine.model.message.FixMessageContext;
import com.honestefforts.fixengine.model.message.components.FixTrailer;
import com.honestefforts.fixengine.model.message.components.FixTrailer.FixTrailerBuilder;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

public class FixTrailerConverter {

  private static final Map<Integer, BiConsumer<FixTrailerBuilder, String>> tagMapping = Map.of(
      10, FixTrailerBuilder::checkSum,
      89, FixTrailerBuilder::signature,
      93, (builder, val) -> builder.signatureLength(parseInt(val))
  );

  public static FixTrailer convert(FixMessageContext context) {
    FixTrailerBuilder builder = FixTrailer.builder();
    tagMapping.forEach((key, builderMapping) ->
        Optional.ofNullable(context.processedMessages().get(key))
            .ifPresent(rawTag -> builderMapping.accept(builder, rawTag.value())));
    return builder.build();
  }
}
