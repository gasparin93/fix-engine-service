package com.honestefforts.fixengine.service.converter;

import com.honestefforts.fixengine.model.message.FixMessage;
import com.honestefforts.fixengine.model.message.tags.RawTag;
import java.util.Map;
import lombok.NonNull;

public class FixMessageFactory {

  public static FixMessage create(@NonNull final Map<String, RawTag> tagMap) {
    switch (tagMap.get("35").value()) {
      case "D":
        return NewOrderSingleConverter.convert(tagMap);
      default:
        throw new IllegalArgumentException("Message type is not supported!");
    }
  }
}
