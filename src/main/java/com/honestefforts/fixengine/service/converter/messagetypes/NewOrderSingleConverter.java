package com.honestefforts.fixengine.service.converter.messagetypes;

import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseBoolean;
import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseChar;
import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseDate;
import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseUtcTimestamp;

import com.honestefforts.fixengine.model.converter.FixConverter;
import com.honestefforts.fixengine.model.message.NewOrderSingle;
import com.honestefforts.fixengine.model.message.tags.RawTag;
import com.honestefforts.fixengine.model.universal.Currency;
import com.honestefforts.fixengine.model.universal.MarketIdentifierCode;
import com.honestefforts.fixengine.service.converter.component.CommissionDataConverter;
import com.honestefforts.fixengine.service.converter.component.DiscretionInstructionsConverter;
import com.honestefforts.fixengine.service.converter.component.FinancialDetailsConverter;
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
import java.util.List;
import java.util.Map;

//TODO: parsers need to be able to handle null, most of these will return null
//TODO: have a Converter interface with getSupportedMessageType() - bean-atize it instead of static
public class NewOrderSingleConverter implements FixConverter<NewOrderSingle> {
  @Override
  public NewOrderSingle convert(Map<String, RawTag> tagMap) {
    return NewOrderSingle.builder()
        .fixHeader(FixHeaderConverter.convert(tagMap))
        .fixTrailer(FixTrailerConverter.convert(tagMap))
        .clordid(tagMap.get("11").value())
        .side(parseChar(tagMap.get("54").value()))
        .transactTime(parseUtcTimestamp(tagMap.get("60").value()))
        .orderType(parseChar(tagMap.get("40").value()))
        .instrument(InstrumentConverter.convert(tagMap))
        .orderQuantityData(OrderQuantityDataConverter.convert(tagMap))
        .secondaryClordid(tagMap.get("526").value())
        .clordidLinkId(tagMap.get("583").value())
        .parties(PartiesConverter.convert(tagMap))
        .tradeOriginationDate(parseDate(tagMap.get("229").value()))
        .tradeDate(parseDate(tagMap.get("75").value()))
        .account(tagMap.get("1").value())
        .accountIdSource(Integer.parseInt(tagMap.get("660").value()))
        .accountType(Integer.parseInt(tagMap.get("581").value()))
        .dayBookingInstruction(parseChar(tagMap.get("589").value()))
        .bookingUnit(parseChar(tagMap.get("590").value()))
        .allocationId(tagMap.get("70").value())
        .numberOfAllocations(Integer.parseInt(tagMap.get("78").value()))
        .allocationAccount(tagMap.get("79").value())
        .allocationAccountIdSource(Integer.parseInt(tagMap.get("661").value()))
        .allocationSettlementCurrency(Currency.valueOf(tagMap.get("736").value()))
        .individualAllocationId(tagMap.get("467").value())
        .nestedParties(NestedPartiesConverter.convert(tagMap))
        .allocatedQuantity(Double.parseDouble(tagMap.get("80").value()))
        .settlementType(parseChar(tagMap.get("63").value()))
        .settlementDate(parseDate(tagMap.get("64").value()))
        .cashMargin(parseChar(tagMap.get("544").value()))
        .clearingFeeIndicator(tagMap.get("635").value())
        .handlingInstructions(parseChar(tagMap.get("21").value()))
        .executionInstructions(List.of(parseChar(tagMap.get("18").value()))) //TODO: actually implement this
        .minimumQuantity(Double.parseDouble(tagMap.get("110").value()))
        .maxFloor(Double.parseDouble(tagMap.get("111").value()))
        .executionDestination(MarketIdentifierCode.valueOf(tagMap.get("100").value()))
        .numberOfTradingSessions(Integer.parseInt(tagMap.get("386").value()))
        .tradingSessionId(tagMap.get("336").value())
        .tradingSessionSubID(tagMap.get("625").value())
        .processCode(parseChar(tagMap.get("81").value()))
        .financingDetails(FinancialDetailsConverter.convert(tagMap))
        .numberOfUnderlyingLegs(Integer.parseInt(tagMap.get("711").value()))
        .underlyingInstrument(UnderlyingInstrumentConverter.convert(tagMap))
        .previousClosePrice(Double.parseDouble(tagMap.get("140").value()))
        .locateRequired(parseBoolean(tagMap.get("114").value()))
        .stipulations(StipulationsConverter.convert(tagMap))
        .quantityType(Integer.parseInt(tagMap.get("854").value()))
        .priceType(Integer.parseInt(tagMap.get("423").value()))
        .price(Double.parseDouble(tagMap.get("44").value()))
        .stopPrice(Double.parseDouble(tagMap.get("99").value()))
        .spreadOrBenchmarkCurveData(SpreadOrBenchmarkCurveDataConverter.convert(tagMap))
        .yieldData(YieldDataConverter.convert(tagMap))
        .currency(Currency.valueOf(tagMap.get("15").value()))
        .complianceId(tagMap.get("376").value())
        .solicitedFlag(parseBoolean(tagMap.get("377").value()))
        .indicationOfInterestId(tagMap.get("23").value())
        .quoteId(tagMap.get("117").value())
        .timeInForce(parseChar(tagMap.get("59").value()))
        .effectiveTime(parseUtcTimestamp(tagMap.get("168").value()))
        .expireDate(parseDate(tagMap.get("432").value()))
        .expireTime(parseUtcTimestamp(tagMap.get("126").value()))
        .gtBookingInst(Integer.parseInt(tagMap.get("427").value()))
        .commissionData(CommissionDataConverter.convert(tagMap))
        .orderCapacity(parseChar(tagMap.get("528").value()))
        .orderRestrictions(List.of(tagMap.get("529").value())) //TODO: actually implement this
        .customerOrderCapacity(Integer.parseInt(tagMap.get("582").value()))
        .forexRequest(parseBoolean(tagMap.get("121").value()))
        .settlementCurrency(Currency.valueOf(tagMap.get("120").value()))
        .bookingType(Integer.parseInt(tagMap.get("775").value()))
        .text(tagMap.get("58").value())
        .encodedTextLength(Integer.parseInt(tagMap.get("354").value()))
        .encodedText(tagMap.get("355").value())
        .settlementDateFuture(parseDate(tagMap.get("193").value()))
        .orderQuantityFuture(Integer.parseInt(tagMap.get("192").value()))
        .priceFuture(Double.parseDouble(tagMap.get("640").value()))
        .positionEffect(parseChar(tagMap.get("77").value()))
        .coveredOrUncovered(Integer.parseInt(tagMap.get("203").value()))
        .maxShow(Integer.parseInt(tagMap.get("210").value()))
        .pegInstructions(PegInstructionsConverter.convert(tagMap))
        .discretionInstructions(DiscretionInstructionsConverter.convert(tagMap))
        .targetStrategy(Integer.parseInt(tagMap.get("847").value()))
        .targetStrategyParameters(tagMap.get("848").value())
        .participationRate(Double.parseDouble(tagMap.get("849").value()))
        .cancellationRights(parseChar(tagMap.get("480").value()))
        .moneyLaunderingStatus(parseChar(tagMap.get("481").value()))
        .registrationId(tagMap.get("513").value())
        .designation(tagMap.get("494").value())
        .build();
  }

  @Override
  public String supports() {
    return "D";
  }
}
