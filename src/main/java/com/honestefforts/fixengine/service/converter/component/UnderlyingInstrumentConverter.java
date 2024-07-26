package com.honestefforts.fixengine.service.converter.component;

import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseChar;
import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseDate;
import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseDouble;
import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseEnum;
import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseInt;
import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseYearMonth;

import com.honestefforts.fixengine.model.message.FixMessageContext;
import com.honestefforts.fixengine.model.message.components.UnderlyingInstrument;
import com.honestefforts.fixengine.model.universal.CountryCode;
import com.honestefforts.fixengine.model.universal.Currency;
import com.honestefforts.fixengine.model.universal.MarketIdentifierCode;

public class UnderlyingInstrumentConverter {
  public static UnderlyingInstrument convert(FixMessageContext context) {
    return UnderlyingInstrument.builder()
        .underlyingSymbol(context.getValueForTag("311"))
        .underlyingSymbolSfx(context.getValueForTag("312"))
        .underlyingSecurityID(context.getValueForTag("309"))
        .underlyingSecurityIDSource(context.getValueForTag("305"))
        .numberOfUnderlyingAlternativeSecurityIds(parseInt(context.getValueForTag("457")))
        .underlyingAlternativeSecurityId(context.getValueForTag("458"))
        .underlyingAlternativeSecurityIdSource(context.getValueForTag("459"))
        .underlyingProduct(parseInt(context.getValueForTag("462")))
        .underlyingClassificationOfFinancialInstrumentsCode(context.getValueForTag("463"))
        .underlyingSecurityType(context.getValueForTag("310"))
        .underlyingSecuritySubType(context.getValueForTag("763"))
        .underlyingMaturityMonthYear(parseYearMonth(context.getValueForTag("313")))
        .underlyingMaturityDate(parseDate(context.getValueForTag("542")))
        .underlyingCouponPaymentDate(parseDate(context.getValueForTag("241")))
        .underlyingIssueDate(parseDate(context.getValueForTag("242")))
        .underlyingRepoCollateralSecurityType(context.getValueForTag("243"))
        .underlyingRepurchaseTerm(parseInt(context.getValueForTag("244")))
        .underlyingRepurchaseRate(parseDouble(context.getValueForTag("245")))
        .underlyingFactor(parseDouble(context.getValueForTag("246")))
        .underlyingCreditRating(context.getValueForTag("256"))
        .underlyingInstrumentRegistry(context.getValueForTag("595"))
        .underlyingCountryOfIssue(parseEnum(CountryCode.class, context.getValueForTag("592")))
        .underlyingStateOrProvinceOfIssue(context.getValueForTag("593"))
        .underlyingLocaleOfIssue(context.getValueForTag("594"))
        .underlyingRedemptionDate(parseDate(context.getValueForTag("247")))
        .underlyingStrikePrice(parseDouble(context.getValueForTag("316")))
        .underlyingStrikeCurrency(parseEnum(Currency.class, context.getValueForTag("941")))
        .underlyingOptionAttribute(parseChar(context.getValueForTag("317")))
        .underlyingContractMultiplier(parseDouble(context.getValueForTag("436")))
        .underlyingCouponRate(parseDouble(context.getValueForTag("435")))
        .underlyingSecurityExchange(parseEnum(MarketIdentifierCode.class, context.getValueForTag("308")))
        .underlyingIssuer(context.getValueForTag("306"))
        .encodedUnderlyingIssuerLength(parseInt(context.getValueForTag("362")))
        .encodedUnderlyingIssuer(context.getValueForTag("363"))
        .underlyingSecurityDescription(context.getValueForTag("307"))
        .encodedUnderlyingSecurityDescriptionLength(parseInt(context.getValueForTag("364")))
        .encodedUnderlyingSecurityDescription(context.getValueForTag("365"))
        .underlyingCommercialPaperProgram(parseInt(context.getValueForTag("877")))
        .underlyingCommercialPaperRegistrationType(context.getValueForTag("878"))
        .underlyingCurrency(parseEnum(Currency.class, context.getValueForTag("318")))
        .underlyingQuantity(parseInt(context.getValueForTag("879")))
        .underlyingPrice(parseDouble(context.getValueForTag("810")))
        .underlyingDirtyPrice(parseDouble(context.getValueForTag("882")))
        .underlyingEndPrice(parseDouble(context.getValueForTag("883")))
        .underlyingStartValue(parseDouble(context.getValueForTag("884")))
        .underlyingCurrentValue(parseDouble(context.getValueForTag("885")))
        .underlyingEndValue(parseDouble(context.getValueForTag("886")))
        .underlyingStipulations(UnderlyingStipulationsConverter.convert(context))
        .build();
  }
}
