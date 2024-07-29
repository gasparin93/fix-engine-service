package com.honestefforts.fixengine.service.converter.component;

import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseBoolean;
import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseChar;
import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseDouble;
import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseEnum;

import com.honestefforts.fixengine.model.message.FixMessageContext;
import com.honestefforts.fixengine.model.message.components.CommissionData;
import com.honestefforts.fixengine.model.message.components.CommissionData.CommissionDataBuilder;
import com.honestefforts.fixengine.model.universal.Currency;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

public class CommissionDataConverter {
  private static final Map<Integer, BiConsumer<CommissionDataBuilder, String>> tagMapping = Map.of(
      12, (builder, val) -> builder.commission(parseDouble(val)),
      13, (builder, val) -> builder.commissionType(parseChar(val)),
      479, (builder, val) -> builder.commissionCurrency(parseEnum(Currency.class, val)),
      497, (builder, val) -> builder.fundRenewCommissionWaived(parseBoolean(val))
  );

  public static CommissionData convert(FixMessageContext context) {
    CommissionDataBuilder builder = CommissionData.builder();
    tagMapping.forEach((key, builderMapping) ->
        Optional.ofNullable(context.processedMessages().get(key))
            .ifPresent(rawTag -> builderMapping.accept(builder, rawTag.value())));
    return builder.build();
  }

}
