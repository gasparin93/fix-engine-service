package com.honestefforts.fixengine.service.converter.component;

import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseBoolean;
import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseInt;
import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseUtcTimestamp;

import com.honestefforts.fixengine.model.message.FixMessageContext;
import com.honestefforts.fixengine.model.message.components.FixHeader;
import com.honestefforts.fixengine.model.message.components.FixHeader.FixHeaderBuilder;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

public class FixHeaderConverter {

  private static final Map<Integer, BiConsumer<FixHeaderBuilder, String>> tagMapping = Map.ofEntries(
      Map.entry(115, FixHeaderBuilder::onBehalfOfCompID),
      Map.entry(116, FixHeaderBuilder::onBehalfOfSubId),
      Map.entry(122, (builder, val) -> builder.originalSendingTime(parseUtcTimestamp(val))),
      Map.entry(128, FixHeaderBuilder::deliverToCompID),
      Map.entry(129, FixHeaderBuilder::deliverToSubId),
      Map.entry(142, FixHeaderBuilder::senderLocationId),
      Map.entry(143, FixHeaderBuilder::targetLocationId),
      Map.entry(144, FixHeaderBuilder::onBehalfOfLocationId),
      Map.entry(145, FixHeaderBuilder::deliverToLocationId),
      Map.entry(212, (builder, val) -> builder.xmlDataLength(parseInt(val))),
      Map.entry(213, FixHeaderBuilder::xmlData),
      Map.entry(34, (builder, val) -> builder.msgSeqNum(parseInt(val))),
      Map.entry(347, FixHeaderBuilder::messageEncoding),
      Map.entry(369, (builder, val) -> builder.lastMsgSeqNumProcessed(parseInt(val))),
      Map.entry(43, (builder, val) -> builder.possibleDuplicationFlag(parseBoolean(val))),
      Map.entry(49, FixHeaderBuilder::senderCompID),
      Map.entry(50, FixHeaderBuilder::senderSubId),
      Map.entry(52, (builder, val) -> builder.sendingTime(parseUtcTimestamp(val))),
      Map.entry(56, FixHeaderBuilder::targetCompID),
      Map.entry(57, FixHeaderBuilder::targetSubId),
      Map.entry(627, (builder, val) -> builder.numberOfHops(parseInt(val))),
      Map.entry(628, FixHeaderBuilder::hopCompId),
      Map.entry(629, (builder, val) -> builder.hopSendingTime(parseUtcTimestamp(val))),
      Map.entry(630, FixHeaderBuilder::hopRefId),
      Map.entry(8, FixHeaderBuilder::version),
      Map.entry(9, (builder, val) -> builder.bodyLength(parseInt(val))),
      Map.entry(90, (builder, val) -> builder.secureDataLength(parseInt(val))),
      Map.entry(91, FixHeaderBuilder::secureData),
      Map.entry(97, (builder, val) -> builder.possibleResend(parseBoolean(val)))
  );

  public static FixHeader convert(FixMessageContext context) {
    FixHeaderBuilder builder = FixHeader.builder();
    tagMapping.forEach((key, builderMapping) ->
        Optional.ofNullable(context.processedMessages().get(key))
            .ifPresent(rawTag -> builderMapping.accept(builder, rawTag.value())));
    builder.messageType(context.messageType());
    return builder.build();
  }
}
