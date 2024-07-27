package com.honestefforts.fixengine.service.converter.component;

import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseDate;
import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseDouble;
import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseInt;

import com.honestefforts.fixengine.model.message.FixMessageContext;
import com.honestefforts.fixengine.model.message.components.YieldData;

public class YieldDataConverter {
  public static YieldData convert(FixMessageContext context) {
    return YieldData.builder()
        .yieldType(context.getValueForTag(235))
        .yield(parseDouble(context.getValueForTag(236)))
        .yieldCalcDate(parseDate(context.getValueForTag(701)))
        .yieldRedemptionDate(parseDate(context.getValueForTag(696)))
        .yieldRedemptionPrice(parseDouble(context.getValueForTag(697)))
        .yieldRedemptionPriceType(parseInt(context.getValueForTag(698)))
        .build();
  }
}
