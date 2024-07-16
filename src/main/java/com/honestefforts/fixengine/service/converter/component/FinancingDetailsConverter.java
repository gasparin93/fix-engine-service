package com.honestefforts.fixengine.service.converter.component;

import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseDate;

import com.honestefforts.fixengine.model.message.components.FinancingDetails;
import com.honestefforts.fixengine.model.message.tags.RawTag;
import com.honestefforts.fixengine.model.universal.Currency;
import java.util.Map;

public class FinancingDetailsConverter {
  public static FinancingDetails convert(Map<String, RawTag> tagMap) {
    return FinancingDetails.builder()
        .agreementDesc(tagMap.get("913").value())
        .agreementId(tagMap.get("914").value())
        .agreementDate(parseDate(tagMap.get("915").value()))
        .agreementCurrency(Currency.valueOf(tagMap.get("918").value()))
        .terminationType(Integer.parseInt(tagMap.get("788").value()))
        .startDate(parseDate(tagMap.get("916").value()))
        .endDate(parseDate(tagMap.get("917").value()))
        .deliveryType(Integer.parseInt(tagMap.get("919").value()))
        .marginRatio(Double.parseDouble(tagMap.get("898").value()))
        .build();
  }
}
