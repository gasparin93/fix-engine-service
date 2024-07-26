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
import com.honestefforts.fixengine.model.message.NewOrderSingle;
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
import org.springframework.stereotype.Component;

@Component
public class NewOrderSingleConverter implements FixConverter<NewOrderSingle> {
  @Override
  public NewOrderSingle convert(FixMessageContext context) {
    return NewOrderSingle.builder()
        .fixHeader(FixHeaderConverter.convert(context))
        .fixTrailer(FixTrailerConverter.convert(context))
        .clordid(context.getValueForTag("11"))
        .side(parseChar(context.getValueForTag("54")))
        .transactTime(parseUtcTimestamp(context.getValueForTag("60")))
        .orderType(parseChar(context.getValueForTag("40")))
        .instrument(InstrumentConverter.convert(context))
        .orderQuantityData(OrderQuantityDataConverter.convert(context))
        .secondaryClordid(context.getValueForTag("526"))
        .clordidLinkId(context.getValueForTag("583"))
        .parties(PartiesConverter.convert(context))
        .tradeOriginationDate(parseDate(context.getValueForTag("229")))
        .tradeDate(parseDate(context.getValueForTag("75")))
        .account(context.getValueForTag("1"))
        .accountIdSource(parseInt(context.getValueForTag("660")))
        .accountType(parseInt(context.getValueForTag("581")))
        .dayBookingInstruction(parseChar(context.getValueForTag("589")))
        .bookingUnit(parseChar(context.getValueForTag("590")))
        .allocationId(context.getValueForTag("70"))
        .numberOfAllocations(parseInt(context.getValueForTag("78")))
        .allocationAccount(context.getValueForTag("79"))
        .allocationAccountIdSource(parseInt(context.getValueForTag("661")))
        .allocationSettlementCurrency(parseEnum(Currency.class, context.getValueForTag("736")))
        .individualAllocationId(context.getValueForTag("467"))
        .nestedParties(NestedPartiesConverter.convert(context))
        .allocatedQuantity(parseDouble(context.getValueForTag("80")))
        .settlementType(parseChar(context.getValueForTag("63")))
        .settlementDate(parseDate(context.getValueForTag("64")))
        .cashMargin(parseChar(context.getValueForTag("544")))
        .clearingFeeIndicator(context.getValueForTag("635"))
        .handlingInstructions(parseChar(context.getValueForTag("21")))
        .executionInstructions(parseSpaceDelimitedList(context.getValueForTag("18"),
            CommonConversionUtil::parseChar)) //TODO: actually implement this
        .minimumQuantity(parseDouble(context.getValueForTag("110")))
        .maxFloor(parseDouble(context.getValueForTag("111")))
        .executionDestination(parseEnum(MarketIdentifierCode.class, context.getValueForTag("100")))
        .numberOfTradingSessions(parseInt(context.getValueForTag("386")))
        .tradingSessionId(context.getValueForTag("336"))
        .tradingSessionSubID(context.getValueForTag("625"))
        .processCode(parseChar(context.getValueForTag("81")))
        .financingDetails(FinancingDetailsConverter.convert(context))
        .numberOfUnderlyingLegs(parseInt(context.getValueForTag("711")))
        .underlyingInstrument(UnderlyingInstrumentConverter.convert(context))
        .previousClosePrice(parseDouble(context.getValueForTag("140")))
        .locateRequired(parseBoolean(context.getValueForTag("114")))
        .stipulations(StipulationsConverter.convert(context))
        .quantityType(parseInt(context.getValueForTag("854")))
        .priceType(parseInt(context.getValueForTag("423")))
        .price(parseDouble(context.getValueForTag("44")))
        .stopPrice(parseDouble(context.getValueForTag("99")))
        .spreadOrBenchmarkCurveData(SpreadOrBenchmarkCurveDataConverter.convert(context))
        .yieldData(YieldDataConverter.convert(context))
        .currency(parseEnum(Currency.class, context.getValueForTag("15")))
        .complianceId(context.getValueForTag("376"))
        .solicitedFlag(parseBoolean(context.getValueForTag("377")))
        .indicationOfInterestId(context.getValueForTag("23"))
        .quoteId(context.getValueForTag("117"))
        .timeInForce(parseChar(context.getValueForTag("59")))
        .effectiveTime(parseUtcTimestamp(context.getValueForTag("168")))
        .expireDate(parseDate(context.getValueForTag("432")))
        .expireTime(parseUtcTimestamp(context.getValueForTag("126")))
        .gtBookingInst(parseInt(context.getValueForTag("427")))
        .commissionData(CommissionDataConverter.convert(context))
        .orderCapacity(parseChar(context.getValueForTag("528")))
        .orderRestrictions(parseSpaceDelimitedList(context.getValueForTag("529"))) //TODO: actually implement this
        .customerOrderCapacity(parseInt(context.getValueForTag("582")))
        .forexRequest(parseBoolean(context.getValueForTag("121")))
        .settlementCurrency(parseEnum(Currency.class, context.getValueForTag("120")))
        .bookingType(parseInt(context.getValueForTag("775")))
        .text(context.getValueForTag("58"))
        .encodedTextLength(parseInt(context.getValueForTag("354")))
        .encodedText(context.getValueForTag("355"))
        .settlementDateFuture(parseDate(context.getValueForTag("193")))
        .orderQuantityFuture(parseInt(context.getValueForTag("192")))
        .priceFuture(parseDouble(context.getValueForTag("640")))
        .positionEffect(parseChar(context.getValueForTag("77")))
        .coveredOrUncovered(parseInt(context.getValueForTag("203")))
        .maxShow(parseInt(context.getValueForTag("210")))
        .pegInstructions(PegInstructionsConverter.convert(context))
        .discretionInstructions(DiscretionInstructionsConverter.convert(context))
        .targetStrategy(parseInt(context.getValueForTag("847")))
        .targetStrategyParameters(context.getValueForTag("848"))
        .participationRate(parseDouble(context.getValueForTag("849")))
        .cancellationRights(parseChar(context.getValueForTag("480")))
        .moneyLaunderingStatus(parseChar(context.getValueForTag("481")))
        .registrationId(context.getValueForTag("513"))
        .designation(context.getValueForTag("494"))
        .build();
  }

  @Override
  public String supports() {
    return "D";
  }
}
