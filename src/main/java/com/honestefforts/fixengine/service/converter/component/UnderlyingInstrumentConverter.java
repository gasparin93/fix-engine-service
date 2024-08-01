package com.honestefforts.fixengine.service.converter.component;

import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseChar;
import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseDate;
import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseDouble;
import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseEnum;
import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseInt;
import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseYearMonth;

import com.honestefforts.fixengine.model.message.FixMessageContext;
import com.honestefforts.fixengine.model.message.components.UnderlyingInstrument;
import com.honestefforts.fixengine.model.message.components.UnderlyingInstrument.UnderlyingInstrumentBuilder;
import com.honestefforts.fixengine.model.universal.CountryCode;
import com.honestefforts.fixengine.model.universal.Currency;
import com.honestefforts.fixengine.model.universal.MarketIdentifierCode;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

public class UnderlyingInstrumentConverter {

  private static final Map<Integer, BiConsumer<UnderlyingInstrumentBuilder, String>> tagMapping = Map.ofEntries(
      Map.entry(241, (builder, val) -> builder.underlyingCouponPaymentDate(parseDate(val))),
      Map.entry(242, (builder, val) -> builder.underlyingIssueDate(parseDate(val))),
      Map.entry(243, UnderlyingInstrumentBuilder::underlyingRepoCollateralSecurityType),
      Map.entry(244, (builder, val) -> builder.underlyingRepurchaseTerm(parseInt(val))),
      Map.entry(245, (builder, val) -> builder.underlyingRepurchaseRate(parseDouble(val))),
      Map.entry(246, (builder, val) -> builder.underlyingFactor(parseDouble(val))),
      Map.entry(247, (builder, val) -> builder.underlyingRedemptionDate(parseDate(val))),
      Map.entry(256, UnderlyingInstrumentBuilder::underlyingCreditRating),
      Map.entry(305, UnderlyingInstrumentBuilder::underlyingSecurityIDSource),
      Map.entry(306, UnderlyingInstrumentBuilder::underlyingIssuer),
      Map.entry(307, UnderlyingInstrumentBuilder::underlyingSecurityDescription),
      Map.entry(308, (builder, val) -> builder.underlyingSecurityExchange(parseEnum(MarketIdentifierCode.class, val))),
      Map.entry(309, UnderlyingInstrumentBuilder::underlyingSecurityID),
      Map.entry(310, UnderlyingInstrumentBuilder::underlyingSecurityType),
      Map.entry(311, UnderlyingInstrumentBuilder::underlyingSymbol),
      Map.entry(312, UnderlyingInstrumentBuilder::underlyingSymbolSfx),
      Map.entry(313, (builder, val) -> builder.underlyingMaturityMonthYear(parseYearMonth(val))),
      Map.entry(316, (builder, val) -> builder.underlyingStrikePrice(parseDouble(val))),
      Map.entry(317, (builder, val) -> builder.underlyingOptionAttribute(parseChar(val))),
      Map.entry(318, (builder, val) -> builder.underlyingCurrency(parseEnum(Currency.class, val))),
      Map.entry(362, (builder, val) -> builder.encodedUnderlyingIssuerLength(parseInt(val))),
      Map.entry(363, UnderlyingInstrumentBuilder::encodedUnderlyingIssuer),
      Map.entry(364, (builder, val) -> builder.encodedUnderlyingSecurityDescriptionLength(parseInt(val))),
      Map.entry(365, UnderlyingInstrumentBuilder::encodedUnderlyingSecurityDescription),
      Map.entry(435, (builder, val) -> builder.underlyingCouponRate(parseDouble(val))),
      Map.entry(436, (builder, val) -> builder.underlyingContractMultiplier(parseDouble(val))),
      Map.entry(457, (builder, val) -> builder.numberOfUnderlyingAlternativeSecurityIds(parseInt(val))),
      Map.entry(458, UnderlyingInstrumentBuilder::underlyingAlternativeSecurityId),
      Map.entry(459, UnderlyingInstrumentBuilder::underlyingAlternativeSecurityIdSource),
      Map.entry(462, (builder, val) -> builder.underlyingProduct(parseInt(val))),
      Map.entry(463, UnderlyingInstrumentBuilder::underlyingClassificationOfFinancialInstrumentsCode),
      Map.entry(542, (builder, val) -> builder.underlyingMaturityDate(parseDate(val))),
      Map.entry(592, (builder, val) -> builder.underlyingCountryOfIssue(parseEnum(CountryCode.class, val))),
      Map.entry(593, UnderlyingInstrumentBuilder::underlyingStateOrProvinceOfIssue),
      Map.entry(594, UnderlyingInstrumentBuilder::underlyingLocaleOfIssue),
      Map.entry(595, UnderlyingInstrumentBuilder::underlyingInstrumentRegistry),
      Map.entry(763, UnderlyingInstrumentBuilder::underlyingSecuritySubType),
      Map.entry(810, (builder, val) -> builder.underlyingPrice(parseDouble(val))),
      Map.entry(877, (builder, val) -> builder.underlyingCommercialPaperProgram(parseInt(val))),
      Map.entry(878, UnderlyingInstrumentBuilder::underlyingCommercialPaperRegistrationType),
      Map.entry(879, (builder, val) -> builder.underlyingQuantity(parseInt(val))),
      Map.entry(882, (builder, val) -> builder.underlyingDirtyPrice(parseDouble(val))),
      Map.entry(883, (builder, val) -> builder.underlyingEndPrice(parseDouble(val))),
      Map.entry(884, (builder, val) -> builder.underlyingStartValue(parseDouble(val))),
      Map.entry(885, (builder, val) -> builder.underlyingCurrentValue(parseDouble(val))),
      Map.entry(886, (builder, val) -> builder.underlyingEndValue(parseDouble(val))),
      Map.entry(941, (builder, val) -> builder.underlyingStrikeCurrency(parseEnum(Currency.class, val)))
  );

  public static UnderlyingInstrument convert(FixMessageContext context) {
    UnderlyingInstrumentBuilder builder = UnderlyingInstrument.builder();
    tagMapping.forEach((key, builderMapping) ->
        Optional.ofNullable(context.processedMessages().get(key))
            .ifPresent(rawTag -> builderMapping.accept(builder, rawTag.value())));
    return builder
        .underlyingStipulations(UnderlyingStipulationsConverter.convert(context))
        .build();
  }
}
