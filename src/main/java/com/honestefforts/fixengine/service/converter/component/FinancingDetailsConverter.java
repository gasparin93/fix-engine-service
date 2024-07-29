package com.honestefforts.fixengine.service.converter.component;

import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseDate;
import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseDouble;
import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseEnum;
import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseInt;

import com.honestefforts.fixengine.model.message.FixMessageContext;
import com.honestefforts.fixengine.model.message.components.FinancingDetails;
import com.honestefforts.fixengine.model.message.components.FinancingDetails.FinancingDetailsBuilder;
import com.honestefforts.fixengine.model.universal.Currency;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

public class FinancingDetailsConverter {

  private static final Map<Integer, BiConsumer<FinancingDetailsBuilder, String>> tagMapping = Map.of(
      788, (builder, val) -> builder.terminationType(parseInt(val)),
      898, (builder, val) -> builder.marginRatio(parseDouble(val)),
      913, FinancingDetailsBuilder::agreementDesc,
      914, FinancingDetailsBuilder::agreementId,
      915, (builder, val) -> builder.agreementDate(parseDate(val)),
      916, (builder, val) -> builder.startDate(parseDate(val)),
      917, (builder, val) -> builder.endDate(parseDate(val)),
      918, (builder, val) -> builder.agreementCurrency(parseEnum(Currency.class, val)),
      919, (builder, val) -> builder.deliveryType(parseInt(val))
  );

  public static FinancingDetails convert(FixMessageContext context) {
    FinancingDetailsBuilder builder = FinancingDetails.builder();
    tagMapping.forEach((key, builderMapping) ->
        Optional.ofNullable(context.processedMessages().get(key))
            .ifPresent(rawTag -> builderMapping.accept(builder, rawTag.value())));
    return builder.build();
  }
}
