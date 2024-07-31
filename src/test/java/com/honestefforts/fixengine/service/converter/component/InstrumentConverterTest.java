package com.honestefforts.fixengine.service.converter.component;

import static com.honestefforts.fixengine.service.converter.TestUtility.getRawTagEntry;
import static com.honestefforts.fixengine.service.converter.TestUtility.parseDateToString;
import static com.honestefforts.fixengine.service.converter.TestUtility.parseYearMonthToString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.honestefforts.fixengine.model.message.FixMessageContext;
import com.honestefforts.fixengine.model.message.components.Instrument;
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

public class InstrumentConverterTest {
  private static final LocalDate today = LocalDate.now();
  private static final YearMonth thisMonth = YearMonth.now();

  @Test
  void convert_happyPath() {

    FixMessageContext context = FixMessageContext.builder()
        .processedMessages(Map.ofEntries(
            getRawTagEntry(55, "string1"),
            getRawTagEntry(65, "string2"),
            getRawTagEntry(48, "string3"),
            getRawTagEntry(22, "string4"),
            getRawTagEntry(454, "1"),
            getRawTagEntry(455, "string5"),
            getRawTagEntry(456, "string6"),
            getRawTagEntry(460, "2"),
            getRawTagEntry(461, "string8"),
            getRawTagEntry(167, "string9"),
            getRawTagEntry(762, "string10"),
            getRawTagEntry(200, parseYearMonthToString(thisMonth)),
            getRawTagEntry(541, parseDateToString(today)),
            getRawTagEntry(224, parseDateToString(today)),
            getRawTagEntry(225, parseDateToString(today)),
            getRawTagEntry(239, "string11"),
            getRawTagEntry(226, "3"),
            getRawTagEntry(227, "1.0"),
            getRawTagEntry(228, "2.0"),
            getRawTagEntry(255, "string12"),
            getRawTagEntry(543, "string13"),
            getRawTagEntry(470, CountryCode.US.name()),
            getRawTagEntry(471, "string14"),
            getRawTagEntry(472, "string15"),
            getRawTagEntry(240, parseDateToString(today)),
            getRawTagEntry(202, "3.0"),
            getRawTagEntry(947, Currency.USD.name()),
            getRawTagEntry(206, "a"),
            getRawTagEntry(231, "4.0"),
            getRawTagEntry(223, "5.0"),
            getRawTagEntry(207, MarketIdentifierCode.ABXX.name()),
            getRawTagEntry(106, "string16"),
            getRawTagEntry(348, "4"),
            getRawTagEntry(349, "string17"),
            getRawTagEntry(107, "string18"),
            getRawTagEntry(350, "5"),
            getRawTagEntry(351, "string19"),
            getRawTagEntry(691, "string20"),
            getRawTagEntry(667, parseYearMonthToString(thisMonth)),
            getRawTagEntry(875, "6"),
            getRawTagEntry(876, "string21"),
            getRawTagEntry(864, "7"),
            getRawTagEntry(865, "8"),
            getRawTagEntry(866, parseDateToString(today)),
            getRawTagEntry(867, "6.0"),
            getRawTagEntry(868, "string22"),
            getRawTagEntry(873, parseDateToString(today)),
            getRawTagEntry(874, parseDateToString(today))
        ))
        .build();

    assertThat(InstrumentConverter.convert(context))
        .usingRecursiveComparison()
        .withStrictTypeChecking()
        .isEqualTo(Instrument.builder()
            .symbol("string1")
            .symbolSfx("string2")
            .securityId("string3")
            .securityIdSource("string4")
            .numberOfAlternativeSecurityIds(1)
            .alternativeSecurityId("string5")
            .alternativeSecurityIdSource("string6")
            .product(2)
            .classificationOfFinancialInstrumentsCode("string8")
            .securityType("string9")
            .securitySubType("string10")
            .maturityMonthYear(thisMonth)
            .maturityDate(today)
            .couponPaymentDate(today)
            .issueDate(today)
            .repoCollateralSecurityType("string11")
            .repurchaseTerm(3)
            .repurchaseRate(1.0)
            .factor(2.0)
            .creditRating("string12")
            .instrumentRegistry("string13")
            .countryOfIssue(CountryCode.US)
            .stateOrProvinceOfIssue("string14")
            .localeOfIssue("string15")
            .redemptionDate(today)
            .strikePrice(3.0)
            .strikeCurrency(Currency.USD)
            .optionAttribute('a')
            .contractMultiplier(4.0)
            .couponRate(5.0)
            .securityExchange(MarketIdentifierCode.ABXX)
            .issuer("string16")
            .encodedIssuerLength(4)
            .encodedIssuer("string17")
            .securityDescription("string18")
            .encodedSecurityDescriptionLength(5)
            .encodedSecurityDescription("string19")
            .pool("string20")
            .contractSettlementMonth(thisMonth)
            .commercialPaperProgram(6)
            .commercialPaperRegistrationType("string21")
            .numberOfEvents(7)
            .eventType(8)
            .eventDate(today)
            .eventPx(6.0)
            .eventText("string22")
            .datedDate(today)
            .interestAccrualDate(today)
            .build());
  }

  @Test
  void convert_emptyMap_expectEmptyObject() {
    FixMessageContext context = FixMessageContext.builder()
        .processedMessages(Map.of())
        .build();

    assertThat(InstrumentConverter.convert(context))
        .usingRecursiveComparison()
        .withStrictTypeChecking()
        .isEqualTo(Instrument.builder().build());
  }

  @Test
  void convert_unsupportedTags_expectEmptyObject() {
    FixMessageContext context = FixMessageContext.builder()
        .processedMessages(Map.ofEntries(
            getRawTagEntry(35, "8"),
            getRawTagEntry(8, null)
        ))
        .build();

    assertThat(InstrumentConverter.convert(context))
        .usingRecursiveComparison()
        .withStrictTypeChecking()
        .isEqualTo(Instrument.builder().build());
  }

  @ParameterizedTest
  @MethodSource("invalidValues")
  void convert_invalidValues_expectExceptions(Map.Entry<Integer, RawTag> tagEntry,
      Class<Throwable> expectedException) {
    FixMessageContext context = FixMessageContext.builder()
        .processedMessages(Map.ofEntries(tagEntry))
        .build();

    assertThatThrownBy(() -> InstrumentConverter.convert(context))
        .isInstanceOf(expectedException);
  }

  private static Stream<Arguments> invalidValues() {
    return Stream.of(
        Arguments.of(getRawTagEntry(454, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(454, null), NumberFormatException.class),
        Arguments.of(getRawTagEntry(460, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(460, null), NumberFormatException.class),
        Arguments.of(getRawTagEntry(200, "5/5/2015"), DateTimeParseException.class),
        Arguments.of(getRawTagEntry(200, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(541, "5/5/2015"), DateTimeParseException.class),
        Arguments.of(getRawTagEntry(541, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(224, "5/5/2015"), DateTimeParseException.class),
        Arguments.of(getRawTagEntry(224, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(225, "5/5/2015"), DateTimeParseException.class),
        Arguments.of(getRawTagEntry(225, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(226, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(226, null), NumberFormatException.class),
        Arguments.of(getRawTagEntry(227, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(227, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(228, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(228, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(470, "ABC"), IllegalArgumentException.class),
        Arguments.of(getRawTagEntry(470, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(206, ""), StringIndexOutOfBoundsException.class),
        Arguments.of(getRawTagEntry(206, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(231, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(231, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(223, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(223, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(207, "ABC_"), IllegalArgumentException.class),
        Arguments.of(getRawTagEntry(207, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(348, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(348, null), NumberFormatException.class),
        Arguments.of(getRawTagEntry(350, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(350, null), NumberFormatException.class),
        Arguments.of(getRawTagEntry(667, "5/5/2015"), DateTimeParseException.class),
        Arguments.of(getRawTagEntry(667, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(875, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(875, null), NumberFormatException.class),
        Arguments.of(getRawTagEntry(864, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(864, null), NumberFormatException.class),
        Arguments.of(getRawTagEntry(865, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(865, null), NumberFormatException.class),
        Arguments.of(getRawTagEntry(866, "5/5/2015"), DateTimeParseException.class),
        Arguments.of(getRawTagEntry(866, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(867, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(867, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(873, "5/5/2015"), DateTimeParseException.class),
        Arguments.of(getRawTagEntry(873, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(874, "5/5/2015"), DateTimeParseException.class),
        Arguments.of(getRawTagEntry(874, null), NullPointerException.class)
    );
  }
}
