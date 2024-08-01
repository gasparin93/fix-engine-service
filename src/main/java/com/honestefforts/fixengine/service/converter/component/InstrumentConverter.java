package com.honestefforts.fixengine.service.converter.component;

import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseChar;
import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseDate;
import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseDouble;
import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseEnum;
import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseInt;
import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseYearMonth;

import com.honestefforts.fixengine.model.message.FixMessageContext;
import com.honestefforts.fixengine.model.message.components.Instrument;
import com.honestefforts.fixengine.model.message.components.Instrument.InstrumentBuilder;
import com.honestefforts.fixengine.model.universal.CountryCode;
import com.honestefforts.fixengine.model.universal.Currency;
import com.honestefforts.fixengine.model.universal.MarketIdentifierCode;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

public class InstrumentConverter {

  private static final Map<Integer, BiConsumer<InstrumentBuilder, String>> tagMapping = Map.ofEntries(
      Map.entry(106, InstrumentBuilder::issuer),
      Map.entry(107, InstrumentBuilder::securityDescription),
      Map.entry(167, InstrumentBuilder::securityType),
      Map.entry(200, (builder, val) -> builder.maturityMonthYear(parseYearMonth(val))),
      Map.entry(202, (builder, val) -> builder.strikePrice(parseDouble(val))),
      Map.entry(206, (builder, val) -> builder.optionAttribute(parseChar(val))),
      Map.entry(207, (builder, val) -> builder.securityExchange(parseEnum(MarketIdentifierCode.class, val))),
      Map.entry(22, InstrumentBuilder::securityIdSource),
      Map.entry(223, (builder, val) -> builder.couponRate(parseDouble(val))),
      Map.entry(224, (builder, val) -> builder.couponPaymentDate(parseDate(val))),
      Map.entry(225, (builder, val) -> builder.issueDate(parseDate(val))),
      Map.entry(226, (builder, val) -> builder.repurchaseTerm(parseInt(val))),
      Map.entry(227, (builder, val) -> builder.repurchaseRate(parseDouble(val))),
      Map.entry(228, (builder, val) -> builder.factor(parseDouble(val))),
      Map.entry(231, (builder, val) -> builder.contractMultiplier(parseDouble(val))),
      Map.entry(239, InstrumentBuilder::repoCollateralSecurityType),
      Map.entry(240, (builder, val) -> builder.redemptionDate(parseDate(val))),
      Map.entry(255, InstrumentBuilder::creditRating),
      Map.entry(348, (builder, val) -> builder.encodedIssuerLength(parseInt(val))),
      Map.entry(349, InstrumentBuilder::encodedIssuer),
      Map.entry(350, (builder, val) -> builder.encodedSecurityDescriptionLength(parseInt(val))),
      Map.entry(351, InstrumentBuilder::encodedSecurityDescription),
      Map.entry(454, (builder, val) -> builder.numberOfAlternativeSecurityIds(parseInt(val))),
      Map.entry(455, InstrumentBuilder::alternativeSecurityId),
      Map.entry(456, InstrumentBuilder::alternativeSecurityIdSource),
      Map.entry(460, (builder, val) -> builder.product(parseInt(val))),
      Map.entry(461, InstrumentBuilder::classificationOfFinancialInstrumentsCode),
      Map.entry(470, (builder, val) -> builder.countryOfIssue(parseEnum(CountryCode.class, val))),
      Map.entry(471, InstrumentBuilder::stateOrProvinceOfIssue),
      Map.entry(472, InstrumentBuilder::localeOfIssue),
      Map.entry(48, InstrumentBuilder::securityId),
      Map.entry(541, (builder, val) -> builder.maturityDate(parseDate(val))),
      Map.entry(543, InstrumentBuilder::instrumentRegistry),
      Map.entry(55, InstrumentBuilder::symbol),
      Map.entry(65, InstrumentBuilder::symbolSfx),
      Map.entry(667, (builder, val) -> builder.contractSettlementMonth(parseYearMonth(val))),
      Map.entry(691, InstrumentBuilder::pool),
      Map.entry(762, InstrumentBuilder::securitySubType),
      Map.entry(864, (builder, val) -> builder.numberOfEvents(parseInt(val))),
      Map.entry(865, (builder, val) -> builder.eventType(parseInt(val))),
      Map.entry(866, (builder, val) -> builder.eventDate(parseDate(val))),
      Map.entry(867, (builder, val) -> builder.eventPx(parseDouble(val))),
      Map.entry(868, InstrumentBuilder::eventText),
      Map.entry(873, (builder, val) -> builder.datedDate(parseDate(val))),
      Map.entry(874, (builder, val) -> builder.interestAccrualDate(parseDate(val))),
      Map.entry(875, (builder, val) -> builder.commercialPaperProgram(parseInt(val))),
      Map.entry(876, InstrumentBuilder::commercialPaperRegistrationType),
      Map.entry(947, (builder, val) -> builder.strikeCurrency(parseEnum(Currency.class, val)))
  );

  public static Instrument convert(FixMessageContext context) {
    InstrumentBuilder builder = Instrument.builder();
    tagMapping.forEach((key, builderMapping) ->
        Optional.ofNullable(context.processedMessages().get(key))
            .ifPresent(rawTag -> builderMapping.accept(builder, rawTag.value())));
    return builder.build();
  }
}
