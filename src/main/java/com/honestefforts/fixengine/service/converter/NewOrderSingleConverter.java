package com.honestefforts.fixengine.service.converter;

import com.honestefforts.fixengine.model.message.NewOrderSingle;
import com.honestefforts.fixengine.model.message.tags.RawTag;
import java.util.Map;

//TODO: have a Converter interface with getSupportedMessageType() - bean-atize it instead of static
public class NewOrderSingleConverter {
  public static NewOrderSingle convert(Map<String, RawTag> tagMap) {
    return NewOrderSingle.builder().build();
  }
}
