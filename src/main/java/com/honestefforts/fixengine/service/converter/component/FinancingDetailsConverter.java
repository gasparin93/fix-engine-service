package com.honestefforts.fixengine.service.converter.component;

import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseDate;
import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseDouble;
import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseEnum;
import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseInt;

import com.honestefforts.fixengine.model.message.FixMessageContext;
import com.honestefforts.fixengine.model.message.components.FinancingDetails;
import com.honestefforts.fixengine.model.universal.Currency;

public class FinancingDetailsConverter {
  public static FinancingDetails convert(FixMessageContext context) {
    return FinancingDetails.builder()
        .agreementDesc(context.getValueForTag(913))
        .agreementId(context.getValueForTag(914))
        .agreementDate(parseDate(context.getValueForTag(915)))
        .agreementCurrency(parseEnum(Currency.class, context.getValueForTag(918)))
        .terminationType(parseInt(context.getValueForTag(788)))
        .startDate(parseDate(context.getValueForTag(916)))
        .endDate(parseDate(context.getValueForTag(917)))
        .deliveryType(parseInt(context.getValueForTag(919)))
        .marginRatio(parseDouble(context.getValueForTag(898)))
        .build();
  }
}
