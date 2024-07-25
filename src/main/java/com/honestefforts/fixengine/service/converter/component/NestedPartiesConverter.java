package com.honestefforts.fixengine.service.converter.component;

import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseChar;

import com.honestefforts.fixengine.model.message.components.NestedParties;
import com.honestefforts.fixengine.model.message.tags.RawTag;
import java.util.Map;

public class NestedPartiesConverter {
  public static NestedParties convert(Map<String, RawTag> tagMap) {
    return NestedParties.builder()
        .numberOfNestedPartyIds(Integer.parseInt(tagMap.get("539").value()))
        .nestedPartyId(tagMap.get("524").value())
        .nestedPartyIdSource(parseChar(tagMap.get("525").value()))
        .nestedPartyRole(Integer.parseInt(tagMap.get("538").value()))
        .numberOfNestedPartySubIds(Integer.parseInt(tagMap.get("804").value()))
        .nestedPartySubId(tagMap.get("545").value())
        .nestedPartySubIdType(Integer.parseInt(tagMap.get("805").value()))
        .build();
  }
}
