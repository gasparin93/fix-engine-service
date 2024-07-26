package com.honestefforts.fixengine.service.converter.component;

import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseBoolean;
import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseInt;
import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseUtcTimestamp;

import com.honestefforts.fixengine.model.message.FixMessageContext;
import com.honestefforts.fixengine.model.message.components.FixHeader;

public class FixHeaderConverter {
  public static FixHeader convert(FixMessageContext context) {
    return FixHeader.builder()
        .version(context.getValueForTag("8"))
        .bodyLength(parseInt(context.getValueForTag("9")))
        .messageType(context.getValueForTag("35"))
        .msgSeqNum(parseInt(context.getValueForTag("34")))
        .sendingTime(parseUtcTimestamp(context.getValueForTag("52")))
        .senderCompID(context.getValueForTag("49"))
        .targetCompID(context.getValueForTag("56"))
        .onBehalfOfCompID(context.getValueForTag("115"))
        .deliverToCompID(context.getValueForTag("128"))
        .secureDataLength(parseInt(context.getValueForTag("90")))
        .secureData(context.getValueForTag("91"))
        .senderSubId(context.getValueForTag("50"))
        .senderLocationId(context.getValueForTag("142"))
        .targetSubId(context.getValueForTag("57"))
        .targetLocationId(context.getValueForTag("143"))
        .onBehalfOfSubId(context.getValueForTag("116"))
        .onBehalfOfLocationId(context.getValueForTag("144"))
        .deliverToSubId(context.getValueForTag("129"))
        .deliverToLocationId(context.getValueForTag("145"))
        .possibleDuplicationFlag(parseBoolean(context.getValueForTag("43")))
        .possibleResend(parseBoolean(context.getValueForTag("97")))
        .originalSendingTime(parseUtcTimestamp(context.getValueForTag("122")))
        .xmlDataLength(parseInt(context.getValueForTag("212")))
        .xmlData(context.getValueForTag("213"))
        .messageEncoding(context.getValueForTag("347"))
        .lastMsgSeqNumProcessed(parseInt(context.getValueForTag("369")))
        .numberOfHops(parseInt(context.getValueForTag("627")))
        .hopCompId(context.getValueForTag("628"))
        .hopSendingTime(parseUtcTimestamp(context.getValueForTag("629")))
        .hopRefId(context.getValueForTag("630"))
        .build();
  }
}
