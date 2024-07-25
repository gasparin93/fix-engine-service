package com.honestefforts.fixengine.service.converter.component;

import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseBoolean;
import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseChar;

import com.honestefforts.fixengine.model.message.components.CommissionData;
import com.honestefforts.fixengine.model.message.tags.RawTag;
import com.honestefforts.fixengine.model.universal.Currency;
import java.util.Map;

public class CommissionDataConverter {
  public static CommissionData convert(Map<String, RawTag> tagMap) {
    return CommissionData.builder()
        .commission(Double.parseDouble(tagMap.get("12").value()))
        .commissionType(parseChar(tagMap.get("13").value()))
        .commissionCurrency(Currency.valueOf(tagMap.get("479").value()))
        .fundRenewCommissionWaived(parseBoolean(tagMap.get("497").value()))
        .build();
  }
}
