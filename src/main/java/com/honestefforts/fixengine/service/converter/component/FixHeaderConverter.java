package com.honestefforts.fixengine.service.converter.component;

import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseBoolean;
import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseUtcTimestamp;

import com.honestefforts.fixengine.model.message.components.FixHeader;
import com.honestefforts.fixengine.model.message.tags.RawTag;
import java.util.Map;

public class FixHeaderConverter {
  public static FixHeader convert(Map<String, RawTag> tagMap) {
    return FixHeader.builder()
        .version(tagMap.get("8").value())
        .bodyLength(Integer.parseInt(tagMap.get("9").value()))
        .messageType(tagMap.get("35").value())
        .msgSeqNum(Integer.parseInt(tagMap.get("34").value()))
        .sendingTime(parseUtcTimestamp(tagMap.get("52").value()))
        .senderCompID(tagMap.get("49").value())
        .targetCompID(tagMap.get("56").value())
        .onBehalfOfCompID(tagMap.get("115").value())
        .deliverToCompID(tagMap.get("128").value())
        .secureDataLength(Integer.parseInt(tagMap.get("90").value()))
        .secureData(tagMap.get("91").value())
        .senderSubId(tagMap.get("50").value())
        .senderLocationId(tagMap.get("142").value())
        .targetSubId(tagMap.get("57").value())
        .targetLocationId(tagMap.get("143").value())
        .onBehalfOfSubId(tagMap.get("116").value())
        .onBehalfOfLocationId(tagMap.get("144").value())
        .deliverToSubId(tagMap.get("129").value())
        .deliverToLocationId(tagMap.get("145").value())
        .possibleDuplicationFlag(parseBoolean(tagMap.get("43").value()))
        .possibleResend(parseBoolean(tagMap.get("97").value()))
        .originalSendingTime(parseUtcTimestamp(tagMap.get("122").value()))
        .xmlDataLength(Integer.parseInt(tagMap.get("212").value()))
        .xmlData(tagMap.get("213").value())
        .messageEncoding(tagMap.get("347").value())
        .lastMsgSeqNumProcessed(Integer.parseInt(tagMap.get("369").value()))
        .numberOfHops(Integer.parseInt(tagMap.get("627").value()))
        .hopCompId(tagMap.get("628").value())
        .hopSendingTime(parseUtcTimestamp(tagMap.get("629").value()))
        .hopRefId(tagMap.get("630").value())
        .build();
  }
}
