package com.honestefforts.fixengine.service.converter.component;

import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseChar;
import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseDate;
import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseYearMonth;

import com.honestefforts.fixengine.model.message.components.UnderlyingInstrument;
import com.honestefforts.fixengine.model.message.tags.RawTag;
import com.honestefforts.fixengine.model.universal.CountryCode;
import com.honestefforts.fixengine.model.universal.Currency;
import com.honestefforts.fixengine.model.universal.MarketIdentifierCode;
import java.util.Map;

public class UnderlyingInstrumentConverter {
  public static UnderlyingInstrument convert(Map<String, RawTag> tagMap) {
    return UnderlyingInstrument.builder()
        .underlyingSymbol(tagMap.get("311").value())
        .underlyingSymbolSfx(tagMap.get("312").value())
        .underlyingSecurityID(tagMap.get("309").value())
        .underlyingSecurityIDSource(tagMap.get("305").value())
        .numberOfUnderlyingAlternativeSecurityIds(Integer.parseInt(tagMap.get("457").value()))
        .underlyingAlternativeSecurityId(tagMap.get("458").value())
        .underlyingAlternativeSecurityIdSource(tagMap.get("459").value())
        .underlyingProduct(Integer.parseInt(tagMap.get("462").value()))
        .underlyingClassificationOfFinancialInstrumentsCode(tagMap.get("463").value())
        .underlyingSecurityType(tagMap.get("310").value())
        .underlyingSecuritySubType(tagMap.get("763").value())
        .underlyingMaturityMonthYear(parseYearMonth(tagMap.get("313").value()))
        .underlyingMaturityDate(parseDate(tagMap.get("542").value()))
        .underlyingCouponPaymentDate(parseDate(tagMap.get("241").value()))
        .underlyingIssueDate(parseDate(tagMap.get("242").value()))
        .underlyingRepoCollateralSecurityType(tagMap.get("243").value())
        .underlyingRepurchaseTerm(Integer.parseInt(tagMap.get("244").value()))
        .underlyingRepurchaseRate(Double.parseDouble(tagMap.get("245").value()))
        .underlyingFactor(Double.parseDouble(tagMap.get("246").value()))
        .underlyingCreditRating(tagMap.get("256").value())
        .underlyingInstrumentRegistry(tagMap.get("595").value())
        .underlyingCountryOfIssue(CountryCode.valueOf(tagMap.get("592").value()))
        .underlyingStateOrProvinceOfIssue(tagMap.get("593").value())
        .underlyingLocaleOfIssue(tagMap.get("594").value())
        .underlyingRedemptionDate(parseDate(tagMap.get("247").value()))
        .underlyingStrikePrice(Double.parseDouble(tagMap.get("316").value()))
        .underlyingStrikeCurrency(Currency.valueOf(tagMap.get("941").value()))
        .underlyingOptionAttribute(parseChar(tagMap.get("317").value()))
        .underlyingContractMultiplier(Double.parseDouble(tagMap.get("436").value()))
        .underlyingCouponRate(Double.parseDouble(tagMap.get("435").value()))
        .underlyingSecurityExchange(MarketIdentifierCode.valueOf(tagMap.get("308").value()))
        .underlyingIssuer(tagMap.get("306").value())
        .encodedUnderlyingIssuerLength(Integer.parseInt(tagMap.get("362").value()))
        .encodedUnderlyingIssuer(tagMap.get("363").value())
        .underlyingSecurityDescription(tagMap.get("307").value())
        .encodedUnderlyingSecurityDescriptionLength(Integer.parseInt(tagMap.get("364").value()))
        .encodedUnderlyingSecurityDescription(tagMap.get("365").value())
        .underlyingCommercialPaperProgram(Integer.parseInt(tagMap.get("877").value()))
        .underlyingCommercialPaperRegistrationType(tagMap.get("878").value())
        .underlyingCurrency(Currency.valueOf(tagMap.get("318").value()))
        .underlyingQuantity(Integer.parseInt(tagMap.get("879").value()))
        .underlyingPrice(Double.parseDouble(tagMap.get("810").value()))
        .underlyingDirtyPrice(Double.parseDouble(tagMap.get("882").value()))
        .underlyingEndPrice(Double.parseDouble(tagMap.get("883").value()))
        .underlyingStartValue(Double.parseDouble(tagMap.get("884").value()))
        .underlyingCurrentValue(Double.parseDouble(tagMap.get("885").value()))
        .underlyingEndValue(Double.parseDouble(tagMap.get("886").value()))
        .underlyingStipulations(UnderlyingStipulationsConverter.convert(tagMap))
        .build();
  }
}
