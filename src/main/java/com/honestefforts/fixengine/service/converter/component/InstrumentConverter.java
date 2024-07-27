package com.honestefforts.fixengine.service.converter.component;

import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseChar;
import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseDate;
import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseDouble;
import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseEnum;
import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseInt;
import static com.honestefforts.fixengine.service.converter.util.CommonConversionUtil.parseYearMonth;

import com.honestefforts.fixengine.model.message.FixMessageContext;
import com.honestefforts.fixengine.model.message.components.Instrument;
import com.honestefforts.fixengine.model.universal.CountryCode;
import com.honestefforts.fixengine.model.universal.Currency;
import com.honestefforts.fixengine.model.universal.MarketIdentifierCode;

public class InstrumentConverter {
  public static Instrument convert(FixMessageContext context) {
    return Instrument.builder()
        .symbol(context.getValueForTag(55))
        .symbolSfx(context.getValueForTag(65))
        .securityId(context.getValueForTag(48))
        .securityIdSource(context.getValueForTag(22))
        .numberOfAlternativeSecurityIds(parseInt(context.getValueForTag(454)))
        .alternativeSecurityId(context.getValueForTag(455))
        .alternativeSecurityIdSource(context.getValueForTag(456))
        .product(parseInt(context.getValueForTag(460)))
        .classificationOfFinancialInstrumentsCode(context.getValueForTag(461))
        .securityType(context.getValueForTag(167))
        .securitySubType(context.getValueForTag(762))
        .maturityMonthYear(parseYearMonth(context.getValueForTag(200)))
        .maturityDate(parseDate(context.getValueForTag(541)))
        .couponPaymentDate(parseDate(context.getValueForTag(224)))
        .issueDate(parseDate(context.getValueForTag(225)))
        .repoCollateralSecurityType(context.getValueForTag(239))
        .repurchaseTerm(parseInt(context.getValueForTag(226)))
        .repurchaseRate(parseDouble(context.getValueForTag(227)))
        .factor(parseDouble(context.getValueForTag(228)))
        .creditRating(context.getValueForTag(255))
        .instrumentRegistry(context.getValueForTag(543))
        .countryOfIssue(parseEnum(CountryCode.class, context.getValueForTag(470)))
        .stateOrProvinceOfIssue(context.getValueForTag(471))
        .localeOfIssue(context.getValueForTag(472))
        .redemptionDate(parseDate(context.getValueForTag(240)))
        .strikePrice(parseDouble(context.getValueForTag(202)))
        .strikeCurrency(parseEnum(Currency.class, context.getValueForTag(947)))
        .optionAttribute(parseChar(context.getValueForTag(206)))
        .contractMultiplier(parseDouble(context.getValueForTag(231)))
        .couponRate(parseDouble(context.getValueForTag(223)))
        .securityExchange(parseEnum(MarketIdentifierCode.class, context.getValueForTag(207)))
        .issuer(context.getValueForTag(106))
        .encodedIssuerLength(parseInt(context.getValueForTag(348)))
        .encodedIssuer(context.getValueForTag(349))
        .securityDescription(context.getValueForTag(107))
        .encodedSecurityDescriptionLength(parseInt(context.getValueForTag(350)))
        .encodedSecurityDescription(context.getValueForTag(351))
        .pool(context.getValueForTag(691))
        .contractSettlementMonth(parseYearMonth(context.getValueForTag(667)))
        .commercialPaperProgram(parseInt(context.getValueForTag(875)))
        .commercialPaperRegistrationType(context.getValueForTag(876))
        .numberOfEvents(parseInt(context.getValueForTag(864)))
        .eventType(parseInt(context.getValueForTag(865)))
        .eventDate(parseDate(context.getValueForTag(866)))
        .eventPx(parseDouble(context.getValueForTag(867)))
        .eventText(context.getValueForTag(868))
        .datedDate(parseDate(context.getValueForTag(873)))
        .interestAccrualDate(parseDate(context.getValueForTag(874)))
        .build();
  }
}
