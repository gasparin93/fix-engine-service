package com.honestefforts.fixengine.service.converter.component;

import static com.honestefforts.fixengine.service.converter.TestUtility.getRawTagEntry;
import static com.honestefforts.fixengine.service.converter.TestUtility.parseDateToString;
import static com.honestefforts.fixengine.service.converter.TestUtility.parseYearMonthToString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.honestefforts.fixengine.model.message.FixMessageContext;
import com.honestefforts.fixengine.model.message.components.UnderlyingInstrument;
import com.honestefforts.fixengine.model.message.components.UnderlyingStipulations;
import com.honestefforts.fixengine.model.message.tags.RawTag;
import com.honestefforts.fixengine.model.universal.CountryCode;
import com.honestefforts.fixengine.model.universal.Currency;
import com.honestefforts.fixengine.model.universal.MarketIdentifierCode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class UnderlyingInstrumentConverterTest {
  YearMonth thisMonth = YearMonth.now();
  LocalDate today = LocalDate.now();

  @Test
  void convert_happyPath() {
    FixMessageContext context = FixMessageContext.builder()
        .processedMessages(Map.ofEntries(
            getRawTagEntry(311, "string1"),
            getRawTagEntry(312, "string2"),
            getRawTagEntry(309, "string3"),
            getRawTagEntry(305, "string4"),
            getRawTagEntry(458, "string5"),
            getRawTagEntry(457, "1"),
            getRawTagEntry(462, "2"),
            getRawTagEntry(459, "string6"),
            getRawTagEntry(463, "string7"),
            getRawTagEntry(310, "string8"),
            getRawTagEntry(763, "string9"),
            getRawTagEntry(313, parseYearMonthToString(thisMonth)),
            getRawTagEntry(542, parseDateToString(today)),
            getRawTagEntry(241, parseDateToString(today)),
            getRawTagEntry(242, parseDateToString(today)),
            getRawTagEntry(243, "string10"),
            getRawTagEntry(244, "3"),
            getRawTagEntry(245, "1.0"),
            getRawTagEntry(246, "2.0"),
            getRawTagEntry(256, "string11"),
            getRawTagEntry(595, "string12"),
            getRawTagEntry(592, CountryCode.US.name()),
            getRawTagEntry(593, "string13"),
            getRawTagEntry(594, "string14"),
            getRawTagEntry(247, parseDateToString(today)),
            getRawTagEntry(316, "3.0"),
            getRawTagEntry(941, Currency.USD.name()),
            getRawTagEntry(317, "a"),
            getRawTagEntry(436, "4.0"),
            getRawTagEntry(435, "5.0"),
            getRawTagEntry(308, MarketIdentifierCode.ABXX.name()),
            getRawTagEntry(306, "string15"),
            getRawTagEntry(362, "4"),
            getRawTagEntry(363, "string16"),
            getRawTagEntry(307, "string17"),
            getRawTagEntry(364, "5"),
            getRawTagEntry(365, "string18"),
            getRawTagEntry(877, "6"),
            getRawTagEntry(878, "string19"),
            getRawTagEntry(318, Currency.USD.name()),
            getRawTagEntry(879, "7"),
            getRawTagEntry(810, "6.0"),
            getRawTagEntry(882, "7.0"),
            getRawTagEntry(883, "8.0"),
            getRawTagEntry(884, "9.0"),
            getRawTagEntry(885, "10.0"),
            getRawTagEntry(886, "11.0")
        ))
        .build();

    assertThat(UnderlyingInstrumentConverter.convert(context))
        .usingRecursiveComparison()
        .withStrictTypeChecking()
        .isEqualTo(UnderlyingInstrument.builder()
            .underlyingSymbol("string1")
            .underlyingSymbolSfx("string2")
            .underlyingSecurityID("string3")
            .underlyingSecurityIDSource("string4")
            .underlyingAlternativeSecurityId("string5")
            .numberOfUnderlyingAlternativeSecurityIds(1)
            .underlyingAlternativeSecurityIdSource("string6")
            .underlyingProduct(2)
            .underlyingClassificationOfFinancialInstrumentsCode("string7")
            .underlyingSecurityType("string8")
            .underlyingSecuritySubType("string9")
            .underlyingMaturityMonthYear(thisMonth)
            .underlyingMaturityDate(today)
            .underlyingCouponPaymentDate(today)
            .underlyingIssueDate(today)
            .underlyingRepoCollateralSecurityType("string10")
            .underlyingRepurchaseTerm(3)
            .underlyingRepurchaseRate(1.0)
            .underlyingFactor(2.0)
            .underlyingCreditRating("string11")
            .underlyingInstrumentRegistry("string12")
            .underlyingCountryOfIssue(CountryCode.US)
            .underlyingStateOrProvinceOfIssue("string13")
            .underlyingLocaleOfIssue("string14")
            .underlyingRedemptionDate(today)
            .underlyingStrikePrice(3.0)
            .underlyingStrikeCurrency(Currency.USD)
            .underlyingOptionAttribute('a')
            .underlyingContractMultiplier(4.0)
            .underlyingCouponRate(5.0)
            .underlyingSecurityExchange(MarketIdentifierCode.ABXX)
            .underlyingIssuer("string15")
            .encodedUnderlyingIssuerLength(4)
            .encodedUnderlyingIssuer("string16")
            .underlyingSecurityDescription("string17")
            .encodedUnderlyingSecurityDescriptionLength(5)
            .encodedUnderlyingSecurityDescription("string18")
            .underlyingCommercialPaperProgram(6)
            .underlyingCommercialPaperRegistrationType("string19")
            .underlyingCurrency(Currency.USD)
            .underlyingQuantity(7)
            .underlyingPrice(6.0)
            .underlyingDirtyPrice(7.0)
            .underlyingEndPrice(8.0)
            .underlyingStartValue(9.0)
            .underlyingCurrentValue(10.0)
            .underlyingEndValue(11.0)
            .underlyingStipulations(UnderlyingStipulations.builder().build())
            .build());
  }

  @Test
  void convert_emptyMap_expectEmptyObject() {
    FixMessageContext context = FixMessageContext.builder()
        .processedMessages(Map.of())
        .build();

    assertThat(UnderlyingInstrumentConverter.convert(context))
        .usingRecursiveComparison()
        .withStrictTypeChecking()
        .isEqualTo(UnderlyingInstrument.builder()
            .underlyingStipulations(UnderlyingStipulations.builder().build())
            .build());
  }

  @Test
  void convert_unsupportedTags_expectEmptyObject() {
    FixMessageContext context = FixMessageContext.builder()
        .processedMessages(Map.ofEntries(
            getRawTagEntry(35, "8"),
            getRawTagEntry(8, null)
        ))
        .build();

    assertThat(UnderlyingInstrumentConverter.convert(context))
        .usingRecursiveComparison()
        .withStrictTypeChecking()
        .isEqualTo(UnderlyingInstrument.builder()
            .underlyingStipulations(UnderlyingStipulations.builder().build())
            .build());
  }

  @ParameterizedTest
  @MethodSource("invalidValues")
  void convert_invalidValues_expectExceptions(Map.Entry<Integer, RawTag> tagEntry,
      Class<Throwable> expectedException) {
    FixMessageContext context = FixMessageContext.builder()
        .processedMessages(Map.ofEntries(tagEntry))
        .build();

    assertThatThrownBy(() -> UnderlyingInstrumentConverter.convert(context))
        .isInstanceOf(expectedException);
  }

  private static Stream<Arguments> invalidValues() {
    return Stream.of(
        Arguments.of(getRawTagEntry(457, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(457, null), NumberFormatException.class),
        Arguments.of(getRawTagEntry(462, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(462, null), NumberFormatException.class),
        Arguments.of(getRawTagEntry(313, "5/5/2015"), DateTimeParseException.class),
        Arguments.of(getRawTagEntry(313, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(542, "5/5/2015"), DateTimeParseException.class),
        Arguments.of(getRawTagEntry(542, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(241, "5/5/2015"), DateTimeParseException.class),
        Arguments.of(getRawTagEntry(241, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(242, "5/5/2015"), DateTimeParseException.class),
        Arguments.of(getRawTagEntry(242, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(244, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(244, null), NumberFormatException.class),
        Arguments.of(getRawTagEntry(245, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(245, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(246, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(246, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(592, "ABC"), IllegalArgumentException.class),
        Arguments.of(getRawTagEntry(592, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(247, "5/5/2015"), DateTimeParseException.class),
        Arguments.of(getRawTagEntry(247, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(316, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(316, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(941, "ABC"), IllegalArgumentException.class),
        Arguments.of(getRawTagEntry(941, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(317, ""), StringIndexOutOfBoundsException.class),
        Arguments.of(getRawTagEntry(317, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(436, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(436, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(435, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(435, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(308, "ABC_"), IllegalArgumentException.class),
        Arguments.of(getRawTagEntry(308, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(362, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(362, null), NumberFormatException.class),
        Arguments.of(getRawTagEntry(364, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(364, null), NumberFormatException.class),
        Arguments.of(getRawTagEntry(877, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(877, null), NumberFormatException.class),
        Arguments.of(getRawTagEntry(318, "ABC"), IllegalArgumentException.class),
        Arguments.of(getRawTagEntry(318, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(879, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(879, null), NumberFormatException.class),
        Arguments.of(getRawTagEntry(810, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(810, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(882, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(882, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(883, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(883, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(884, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(884, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(885, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(885, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(886, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(886, null), NullPointerException.class)
    );
  }
}
