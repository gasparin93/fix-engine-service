package com.honestefforts.fixengine.service.converter.component;

import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseChar;
import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseDouble;
import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseInt;

import com.honestefforts.fixengine.model.message.FixMessageContext;
import com.honestefforts.fixengine.model.message.components.OrderQuantityData;

public class OrderQuantityDataConverter {
  public static OrderQuantityData convert(FixMessageContext context) {
    return OrderQuantityData.builder()
        .orderQuantity(parseInt(context.getValueForTag(38)))
        .cashOrderQuantity(parseInt(context.getValueForTag(152)))
        .orderPercent(parseDouble(context.getValueForTag(516)))
        .roundingDirection(parseChar(context.getValueForTag(468)))
        .roundingModulus(parseDouble(context.getValueForTag(469)))
        .build();
  }
}
