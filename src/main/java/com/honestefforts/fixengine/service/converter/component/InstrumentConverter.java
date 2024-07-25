package com.honestefforts.fixengine.service.converter.component;

import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseChar;
import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseDate;
import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseYearMonth;

import com.honestefforts.fixengine.model.message.components.Instrument;
import com.honestefforts.fixengine.model.message.tags.RawTag;
import com.honestefforts.fixengine.model.universal.CountryCode;
import com.honestefforts.fixengine.model.universal.Currency;
import com.honestefforts.fixengine.model.universal.MarketIdentifierCode;
import java.util.Map;

public class InstrumentConverter {
  public static Instrument convert(Map<String, RawTag> tagMap) {
    return Instrument.builder()
        .symbol(tagMap.get("55").value())
        .symbolSfx(tagMap.get("65").value())
        .securityId(tagMap.get("48").value())
        .securityIdSource(tagMap.get("22").value())
        .numberOfAlternativeSecurityIds(Integer.parseInt(tagMap.get("454").value()))
        .alternativeSecurityId(tagMap.get("455").value())
        .alternativeSecurityIdSource(tagMap.get("456").value())
        .product(Integer.parseInt(tagMap.get("460").value()))
        .classificationOfFinancialInstrumentsCode(tagMap.get("461").value())
        .securityType(tagMap.get("167").value())
        .securitySubType(tagMap.get("762").value())
        .maturityMonthYear(parseYearMonth(tagMap.get("200").value()))
        .maturityDate(parseDate(tagMap.get("541").value()))
        .couponPaymentDate(parseDate(tagMap.get("224").value()))
        .issueDate(parseDate(tagMap.get("225").value()))
        .repoCollateralSecurityType(tagMap.get("239").value())
        .repurchaseTerm(Integer.parseInt(tagMap.get("226").value()))
        .repurchaseRate(Double.parseDouble(tagMap.get("227").value()))
        .factor(Double.parseDouble(tagMap.get("228").value()))
        .creditRating(tagMap.get("255").value())
        .instrumentRegistry(tagMap.get("543").value())
        .countryOfIssue(CountryCode.valueOf(tagMap.get("470").value()))
        .stateOrProvinceOfIssue(tagMap.get("471").value())
        .localeOfIssue(tagMap.get("472").value())
        .redemptionDate(parseDate(tagMap.get("240").value()))
        .strikePrice(Double.parseDouble(tagMap.get("202").value()))
        .strikeCurrency(Currency.valueOf(tagMap.get("947").value()))
        .optionAttribute(parseChar(tagMap.get("206").value()))
        .contractMultiplier(Double.parseDouble(tagMap.get("231").value()))
        .couponRate(Double.parseDouble(tagMap.get("223").value()))
        .securityExchange(MarketIdentifierCode.valueOf(tagMap.get("207").value()))
        .issuer(tagMap.get("106").value())
        .encodedIssuerLength(Integer.parseInt(tagMap.get("348").value()))
        .encodedIssuer(tagMap.get("349").value())
        .securityDescription(tagMap.get("107").value())
        .encodedSecurityDescriptionLength(Integer.parseInt(tagMap.get("350").value()))
        .encodedSecurityDescription(tagMap.get("351").value())
        .pool(tagMap.get("691").value())
        .contractSettlementMonth(parseYearMonth(tagMap.get("667").value()))
        .commercialPaperProgram(Integer.parseInt(tagMap.get("875").value()))
        .commercialPaperRegistrationType(tagMap.get("876").value())
        .numberOfEvents(Integer.parseInt(tagMap.get("864").value()))
        .eventType(Integer.parseInt(tagMap.get("865").value()))
        .eventDate(parseDate(tagMap.get("866").value()))
        .eventPx(Double.parseDouble(tagMap.get("867").value()))
        .eventText(tagMap.get("868").value())
        .datedDate(parseDate(tagMap.get("873").value()))
        .interestAccrualDate(parseDate(tagMap.get("874").value()))
        .build();
  }
}
