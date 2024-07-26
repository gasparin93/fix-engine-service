package com.honestefforts.fixengine.service.converter.component;

import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseInt;

import com.honestefforts.fixengine.model.message.FixMessageContext;
import com.honestefforts.fixengine.model.message.components.FixTrailer;

public class FixTrailerConverter {
  public static FixTrailer convert(FixMessageContext context) {
    return FixTrailer.builder()
        .checkSum(context.getValueForTag("10"))
        .signatureLength(parseInt(context.getValueForTag("93")))
        .signature(context.getValueForTag("89"))
        .build();
  }
}
