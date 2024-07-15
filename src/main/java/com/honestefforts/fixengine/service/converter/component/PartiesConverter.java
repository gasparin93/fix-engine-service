package com.honestefforts.fixengine.service.converter.component;

import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseChar;

import com.honestefforts.fixengine.model.message.components.Parties;
import com.honestefforts.fixengine.model.message.tags.RawTag;
import java.util.Map;

public class PartiesConverter {
  public static Parties convert(Map<String, RawTag> tagMap) {
    return Parties.builder()
        .numberOfPartyIds(Integer.parseInt(tagMap.get("453").value()))
        .partyId(tagMap.get("448").value())
        .partyIdSource(parseChar(tagMap.get("447").value()))
        .partyRole(Integer.parseInt(tagMap.get("452").value()))
        .numberOfPartySubIds(Integer.parseInt(tagMap.get("802").value()))
        .partySubId(tagMap.get("523").value())
        .partySubIdType(Integer.parseInt(tagMap.get("803").value()))
        .build();
  }
}
