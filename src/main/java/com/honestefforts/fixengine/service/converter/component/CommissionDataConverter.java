package com.honestefforts.fixengine.service.converter.component;

import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseBoolean;
import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseChar;
import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseDouble;
import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseEnum;

import com.honestefforts.fixengine.model.message.FixMessageContext;
import com.honestefforts.fixengine.model.message.components.CommissionData;
import com.honestefforts.fixengine.model.universal.Currency;

public class CommissionDataConverter {
  public static CommissionData convert(FixMessageContext context) {
    return CommissionData.builder()
        .commission(parseDouble(context.getValueForTag("12")))
        .commissionType(parseChar(context.getValueForTag("13")))
        .commissionCurrency(parseEnum(Currency.class, context.getValueForTag("479")))
        .fundRenewCommissionWaived(parseBoolean(context.getValueForTag("497")))
        .build();
  }
}
