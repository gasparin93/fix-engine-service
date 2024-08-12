package com.honestefforts.fixengine.service.converter.messagetypes;

import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseBoolean;
import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseChar;
import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseDate;
import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseDouble;
import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseEnum;
import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseInt;
import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseSpaceDelimitedList;
import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseUtcTimestamp;

import com.honestefforts.fixengine.model.converter.FixConverter;
import com.honestefforts.fixengine.model.message.FixMessageContext;
import com.honestefforts.fixengine.model.message.types.NewOrderSingle;
import com.honestefforts.fixengine.model.message.types.NewOrderSingle.NewOrderSingleBuilder;
import com.honestefforts.fixengine.model.universal.Currency;
import com.honestefforts.fixengine.model.universal.MarketIdentifierCode;
import com.honestefforts.fixengine.service.converter.component.CommissionDataConverter;
import com.honestefforts.fixengine.service.converter.component.DiscretionInstructionsConverter;
import com.honestefforts.fixengine.service.converter.component.FinancingDetailsConverter;
import com.honestefforts.fixengine.service.converter.component.FixHeaderConverter;
import com.honestefforts.fixengine.service.converter.component.FixTrailerConverter;
import com.honestefforts.fixengine.service.converter.component.InstrumentConverter;
import com.honestefforts.fixengine.service.converter.component.NestedPartiesConverter;
import com.honestefforts.fixengine.service.converter.component.OrderQuantityDataConverter;
import com.honestefforts.fixengine.service.converter.component.PartiesConverter;
import com.honestefforts.fixengine.service.converter.component.PegInstructionsConverter;
import com.honestefforts.fixengine.service.converter.component.SpreadOrBenchmarkCurveDataConverter;
import com.honestefforts.fixengine.service.converter.component.StipulationsConverter;
import com.honestefforts.fixengine.service.converter.component.UnderlyingInstrumentConverter;
import com.honestefforts.fixengine.service.converter.component.YieldDataConverter;
import com.honestefforts.fixengine.service.converter.util.CommonConversionUtil;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import org.springframework.stereotype.Component;

@Component
public class NewOrderSingleConverter implements FixConverter<NewOrderSingle> {

  private static final Map<Integer, BiConsumer<NewOrderSingleBuilder, String>> tagMapping = Map.ofEntries(
      Map.entry(1, NewOrderSingleBuilder::account),
      Map.entry(11, NewOrderSingleBuilder::clordid),
      Map.entry(15, (builder, val) -> builder.currency(parseEnum(Currency.class, val))),
      Map.entry(18, (builder, val) -> builder.executionInstructions(
          parseSpaceDelimitedList(val, CommonConversionUtil::parseChar))), //TODO: actually implement this
      Map.entry(21, (builder, val) -> builder.handlingInstructions(parseChar(val))),
      Map.entry(23, NewOrderSingleBuilder::indicationOfInterestId),
      Map.entry(40, (builder, val) -> builder.orderType(parseChar(val))),
      Map.entry(44, (builder, val) -> builder.price(parseDouble(val))),
      Map.entry(54, (builder, val) -> builder.side(parseChar(val))),
      Map.entry(58, NewOrderSingleBuilder::text),
      Map.entry(59, (builder, val) -> builder.timeInForce(parseChar(val))),
      Map.entry(60, (builder, val) -> builder.transactTime(parseUtcTimestamp(val))),
      Map.entry(63, (builder, val) -> builder.settlementType(parseChar(val))),
      Map.entry(64, (builder, val) -> builder.settlementDate(parseDate(val))),
      Map.entry(70, NewOrderSingleBuilder::allocationId),
      Map.entry(75, (builder, val) -> builder.tradeDate(parseDate(val))),
      Map.entry(77, (builder, val) -> builder.positionEffect(parseChar(val))),
      Map.entry(78, (builder, val) -> builder.numberOfAllocations(parseInt(val))),
      Map.entry(79, NewOrderSingleBuilder::allocationAccount),
      Map.entry(80, (builder, val) -> builder.allocatedQuantity(parseDouble(val))),
      Map.entry(81, (builder, val) -> builder.processCode(parseChar(val))),
      Map.entry(99, (builder, val) -> builder.stopPrice(parseDouble(val))),
      Map.entry(100, (builder, val) -> builder.executionDestination(
          parseEnum(MarketIdentifierCode.class, val))),
      Map.entry(110, (builder, val) -> builder.minimumQuantity(parseDouble(val))),
      Map.entry(111, (builder, val) -> builder.maxFloor(parseDouble(val))),
      Map.entry(114, (builder, val) -> builder.locateRequired(parseBoolean(val))),
      Map.entry(117, NewOrderSingleBuilder::quoteId),
      Map.entry(120, (builder, val) -> builder.settlementCurrency(parseEnum(Currency.class, val))),
      Map.entry(121, (builder, val) -> builder.forexRequest(parseBoolean(val))),
      Map.entry(126, (builder, val) -> builder.expireTime(parseUtcTimestamp(val))),
      Map.entry(140, (builder, val) -> builder.previousClosePrice(parseDouble(val))),
      Map.entry(168, (builder, val) -> builder.effectiveTime(parseUtcTimestamp(val))),
      Map.entry(192, (builder, val) -> builder.orderQuantityFuture(parseInt(val))),
      Map.entry(193, (builder, val) -> builder.settlementDateFuture(parseDate(val))),
      Map.entry(203, (builder, val) -> builder.coveredOrUncovered(parseInt(val))),
      Map.entry(210, (builder, val) -> builder.maxShow(parseInt(val))),
      Map.entry(229, (builder, val) -> builder.tradeOriginationDate(parseDate(val))),
      Map.entry(336, NewOrderSingleBuilder::tradingSessionId),
      Map.entry(354, (builder, val) -> builder.encodedTextLength(parseInt(val))),
      Map.entry(355, NewOrderSingleBuilder::encodedText),
      Map.entry(376, NewOrderSingleBuilder::complianceId),
      Map.entry(377, (builder, val) -> builder.solicitedFlag(parseBoolean(val))),
      Map.entry(386, (builder, val) -> builder.numberOfTradingSessions(parseInt(val))),
      Map.entry(423, (builder, val) -> builder.priceType(parseInt(val))),
      Map.entry(427, (builder, val) -> builder.gtBookingInst(parseInt(val))),
      Map.entry(432, (builder, val) -> builder.expireDate(parseDate(val))),
      Map.entry(467, NewOrderSingleBuilder::individualAllocationId),
      Map.entry(480, (builder, val) -> builder.cancellationRights(parseChar(val))),
      Map.entry(481, (builder, val) -> builder.moneyLaunderingStatus(parseChar(val))),
      Map.entry(494, NewOrderSingleBuilder::designation),
      Map.entry(513, NewOrderSingleBuilder::registrationId),
      Map.entry(526, NewOrderSingleBuilder::secondaryClordid),
      Map.entry(528, (builder, val) -> builder.orderCapacity(parseChar(val))),
      Map.entry(529, (builder, val) -> builder.orderRestrictions(parseSpaceDelimitedList(val))), //TODO: actually implement this
      Map.entry(544, (builder, val) -> builder.cashMargin(parseChar(val))),
      Map.entry(581, (builder, val) -> builder.accountType(parseInt(val))),
      Map.entry(582, (builder, val) -> builder.customerOrderCapacity(parseInt(val))),
      Map.entry(583, NewOrderSingleBuilder::clordidLinkId),
      Map.entry(589, (builder, val) -> builder.dayBookingInstruction(parseChar(val))),
      Map.entry(590, (builder, val) -> builder.bookingUnit(parseChar(val))),
      Map.entry(625, NewOrderSingleBuilder::tradingSessionSubID),
      Map.entry(635, NewOrderSingleBuilder::clearingFeeIndicator),
      Map.entry(640, (builder, val) -> builder.priceFuture(parseDouble(val))),
      Map.entry(660, (builder, val) -> builder.accountIdSource(parseInt(val))),
      Map.entry(661, (builder, val) -> builder.allocationAccountIdSource(parseInt(val))),
      Map.entry(711, (builder, val) -> builder.numberOfUnderlyingLegs(parseInt(val))),
      Map.entry(736, (builder, val) -> builder.allocationSettlementCurrency(
          parseEnum(Currency.class, val))),
      Map.entry(775, (builder, val) -> builder.bookingType(parseInt(val))),
      Map.entry(847, (builder, val) -> builder.targetStrategy(parseInt(val))),
      Map.entry(848, NewOrderSingleBuilder::targetStrategyParameters),
      Map.entry(849, (builder, val) -> builder.participationRate(parseDouble(val))),
      Map.entry(854, (builder, val) -> builder.quantityType(parseInt(val)))
  );

  @Override
  public NewOrderSingle convert(FixMessageContext context) {
    NewOrderSingleBuilder<?, ?> builder = NewOrderSingle.builder();
    tagMapping.forEach((key, builderMapping) ->
        Optional.ofNullable(context.processedMessages().get(key))
            .ifPresent(rawTag -> builderMapping.accept(builder, rawTag.value())));
    return builder
        .instrument(InstrumentConverter.convert(context))
        .commissionData(CommissionDataConverter.convert(context))
        .discretionInstructions(DiscretionInstructionsConverter.convert(context))
        .financingDetails(FinancingDetailsConverter.convert(context))
        .fixHeader(FixHeaderConverter.convert(context))
        .fixTrailer(FixTrailerConverter.convert(context))
        .nestedParties(NestedPartiesConverter.convert(context))
        .orderQuantityData(OrderQuantityDataConverter.convert(context))
        .parties(PartiesConverter.convert(context))
        .pegInstructions(PegInstructionsConverter.convert(context))
        .spreadOrBenchmarkCurveData(SpreadOrBenchmarkCurveDataConverter.convert(context))
        .stipulations(StipulationsConverter.convert(context))
        .underlyingInstrument(UnderlyingInstrumentConverter.convert(context))
        .yieldData(YieldDataConverter.convert(context))
        .build();
  }

  @Override
  public String supports() {
    return "D";
  }
}
