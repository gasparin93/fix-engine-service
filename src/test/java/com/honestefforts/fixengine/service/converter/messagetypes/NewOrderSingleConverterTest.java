package com.honestefforts.fixengine.service.converter.messagetypes;

import static com.honestefforts.fixengine.service.TestUtility.getRawTagEntry;
import static com.honestefforts.fixengine.service.TestUtility.parseDateTimeMsToString;
import static com.honestefforts.fixengine.service.TestUtility.parseDateTimeToString;
import static com.honestefforts.fixengine.service.TestUtility.parseDateToString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

import com.honestefforts.fixengine.model.message.FixMessageContext;
import com.honestefforts.fixengine.model.message.NewOrderSingle;
import com.honestefforts.fixengine.model.message.components.CommissionData;
import com.honestefforts.fixengine.model.message.components.DiscretionInstructions;
import com.honestefforts.fixengine.model.message.components.FinancingDetails;
import com.honestefforts.fixengine.model.message.components.FixHeader;
import com.honestefforts.fixengine.model.message.components.FixTrailer;
import com.honestefforts.fixengine.model.message.components.Instrument;
import com.honestefforts.fixengine.model.message.components.NestedParties;
import com.honestefforts.fixengine.model.message.components.OrderQuantityData;
import com.honestefforts.fixengine.model.message.components.Parties;
import com.honestefforts.fixengine.model.message.components.PegInstructions;
import com.honestefforts.fixengine.model.message.components.SpreadOrBenchmarkCurveData;
import com.honestefforts.fixengine.model.message.components.Stipulations;
import com.honestefforts.fixengine.model.message.components.UnderlyingInstrument;
import com.honestefforts.fixengine.model.message.components.YieldData;
import com.honestefforts.fixengine.model.message.tags.RawTag;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.MockedStatic;

public class NewOrderSingleConverterTest {

  NewOrderSingleConverter converter = new NewOrderSingleConverter();

  private static final LocalDate today = LocalDate.now();
  private static final LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
  private static final LocalDateTime fiveMinutesAgo = now.minusMinutes(5).truncatedTo(ChronoUnit.MILLIS);

  private static MockedStatic<FixHeaderConverter> fixHeaderConverter;
  private static MockedStatic<FixTrailerConverter> fixTrailerConverter;
  private static MockedStatic<InstrumentConverter> instrumentConverter;
  private static MockedStatic<OrderQuantityDataConverter> orderQuantityDataConverter;
  private static MockedStatic<PartiesConverter> partiesConverter;
  private static MockedStatic<NestedPartiesConverter> nestedPartiesConverter;
  private static MockedStatic<UnderlyingInstrumentConverter> underlyingInstrumentConverter;
  private static MockedStatic<FinancingDetailsConverter> financingDetailsConverter;
  private static MockedStatic<SpreadOrBenchmarkCurveDataConverter> spreadOrBenchmarkCurveDataConverter;
  private static MockedStatic<YieldDataConverter> yieldDataConverter;
  private static MockedStatic<StipulationsConverter> stipulationsConverter;
  private static MockedStatic<PegInstructionsConverter> pegInstructionsConverter;
  private static MockedStatic<DiscretionInstructionsConverter> discretionInstructionsConverter;
  private static MockedStatic<CommissionDataConverter> commissionDataConverter;

  @BeforeAll
  public static void setUp() {
    fixHeaderConverter = mockStatic(FixHeaderConverter.class);
    fixTrailerConverter = mockStatic(FixTrailerConverter.class);
    instrumentConverter = mockStatic(InstrumentConverter.class);
    orderQuantityDataConverter = mockStatic(OrderQuantityDataConverter.class);
    partiesConverter = mockStatic(PartiesConverter.class);
    nestedPartiesConverter = mockStatic(NestedPartiesConverter.class);
    underlyingInstrumentConverter = mockStatic(UnderlyingInstrumentConverter.class);
    financingDetailsConverter = mockStatic(FinancingDetailsConverter.class);
    spreadOrBenchmarkCurveDataConverter = mockStatic(SpreadOrBenchmarkCurveDataConverter.class);
    yieldDataConverter = mockStatic(YieldDataConverter.class);
    stipulationsConverter = mockStatic(StipulationsConverter.class);
    pegInstructionsConverter = mockStatic(PegInstructionsConverter.class);
    discretionInstructionsConverter = mockStatic(DiscretionInstructionsConverter.class);
    commissionDataConverter = mockStatic(CommissionDataConverter.class);

    fixHeaderConverter.when(() -> FixHeaderConverter.convert(any())).thenReturn(mock(FixHeader.class));
    fixTrailerConverter.when(() -> FixTrailerConverter.convert(any())).thenReturn(mock(FixTrailer.class));
    instrumentConverter.when(() -> InstrumentConverter.convert(any())).thenReturn(mock(Instrument.class));
    orderQuantityDataConverter.when(() -> OrderQuantityDataConverter.convert(any())).thenReturn(mock(OrderQuantityData.class));
    partiesConverter.when(() -> PartiesConverter.convert(any())).thenReturn(mock(Parties.class));
    nestedPartiesConverter.when(() -> NestedPartiesConverter.convert(any())).thenReturn(mock(NestedParties.class));
    underlyingInstrumentConverter.when(() -> UnderlyingInstrumentConverter.convert(any())).thenReturn(mock(UnderlyingInstrument.class));
    financingDetailsConverter.when(() -> FinancingDetailsConverter.convert(any())).thenReturn(mock(FinancingDetails.class));
    spreadOrBenchmarkCurveDataConverter.when(() -> SpreadOrBenchmarkCurveDataConverter.convert(any())).thenReturn(mock(SpreadOrBenchmarkCurveData.class));
    yieldDataConverter.when(() -> YieldDataConverter.convert(any())).thenReturn(mock(YieldData.class));
    stipulationsConverter.when(() -> StipulationsConverter.convert(any())).thenReturn(mock(Stipulations.class));
    pegInstructionsConverter.when(() -> PegInstructionsConverter.convert(any())).thenReturn(mock(PegInstructions.class));
    discretionInstructionsConverter.when(() -> DiscretionInstructionsConverter.convert(any())).thenReturn(mock(DiscretionInstructions.class));
    commissionDataConverter.when(() -> CommissionDataConverter.convert(any())).thenReturn(mock(CommissionData.class));
  }

  @AfterAll
  public static void tearDown() {
    fixHeaderConverter.close();
    fixTrailerConverter.close();
    instrumentConverter.close();
    orderQuantityDataConverter.close();
    partiesConverter.close();
    nestedPartiesConverter.close();
    underlyingInstrumentConverter.close();
    financingDetailsConverter.close();
    spreadOrBenchmarkCurveDataConverter.close();
    yieldDataConverter.close();
    stipulationsConverter.close();
    pegInstructionsConverter.close();
    discretionInstructionsConverter.close();
    commissionDataConverter.close();
  }

  @Test
  void convert_happyPath() {
    FixMessageContext context = FixMessageContext.builder()
        .processedMessages(Map.ofEntries(
            getRawTagEntry(11, "string1"),
            getRawTagEntry(54, "a"),
            getRawTagEntry(60, parseDateTimeToString(now)),
            getRawTagEntry(40, "b"),
            getRawTagEntry(526, "string2"),
            getRawTagEntry(583, "string3"),
            getRawTagEntry(229, parseDateToString(today)),
            getRawTagEntry(75, parseDateToString(today)),
            getRawTagEntry(1, "string4"),
            getRawTagEntry(660, "1"),
            getRawTagEntry(581, "2"),
            getRawTagEntry(589, "c"),
            getRawTagEntry(590, "d"),
            getRawTagEntry(70, "string5"),
            getRawTagEntry(78, "2"),
            getRawTagEntry(79, "string6"),
            getRawTagEntry(661, "3"),
            getRawTagEntry(736, Currency.USD.name()),
            getRawTagEntry(467, "string7"),
            getRawTagEntry(80, "1.0"),
            getRawTagEntry(63, "e"),
            getRawTagEntry(64, parseDateToString(today)),
            getRawTagEntry(544, "f"),
            getRawTagEntry(625, "string8"),
            getRawTagEntry(21, "g"),
            getRawTagEntry(18, "h i j k"),
            getRawTagEntry(110, "2.0"),
            getRawTagEntry(111, "3.0"),
            getRawTagEntry(100, MarketIdentifierCode.ABXX.name()),
            getRawTagEntry(386, "4"),
            getRawTagEntry(336, "string9"),
            getRawTagEntry(635, "string10"),
            getRawTagEntry(81, "l"),
            getRawTagEntry(711, "5"),
            getRawTagEntry(140, "4.0"),
            getRawTagEntry(114, "Y"),
            getRawTagEntry(854, "6"),
            getRawTagEntry(423, "7"),
            getRawTagEntry(44, "5.0"),
            getRawTagEntry(99, "6.0"),
            getRawTagEntry(15, Currency.USD.name()),
            getRawTagEntry(376, "string11"),
            getRawTagEntry(377, "N"),
            getRawTagEntry(23, "string12"),
            getRawTagEntry(117, "string13"),
            getRawTagEntry(59, "m"),
            getRawTagEntry(168, parseDateTimeMsToString(fiveMinutesAgo)),
            getRawTagEntry(432, parseDateToString(today)),
            getRawTagEntry(126, parseDateTimeToString(now)),
            getRawTagEntry(427, "8"),
            getRawTagEntry(528, "n"),
            getRawTagEntry(529, "str1 str2 str3"),
            getRawTagEntry(582, "9"),
            getRawTagEntry(121, "Y"),
            getRawTagEntry(120, Currency.USD.name()),
            getRawTagEntry(775, "10"),
            getRawTagEntry(58, "string14"),
            getRawTagEntry(354, "11"),
            getRawTagEntry(355, "string15"),
            getRawTagEntry(193, parseDateToString(today)),
            getRawTagEntry(192, "12"),
            getRawTagEntry(640, "7.0"),
            getRawTagEntry(77, "o"),
            getRawTagEntry(203, "13"),
            getRawTagEntry(210, "14"),
            getRawTagEntry(847, "15"),
            getRawTagEntry(848, "string16"),
            getRawTagEntry(849, "8.0"),
            getRawTagEntry(480, "p"),
            getRawTagEntry(481, "q"),
            getRawTagEntry(513, "string17"),
            getRawTagEntry(494, "string18")
        ))
        .build();

    assertThat(converter.convert(context))
        .usingRecursiveComparison()
        .withStrictTypeChecking()
        .isEqualTo(NewOrderSingle.builder()
            .clordid("string1")
            .side('a')
            .transactTime(now)
            .orderType('b')
            .secondaryClordid("string2")
            .clordidLinkId("string3")
            .tradeOriginationDate(today)
            .tradeDate(today)
            .account("string4")
            .accountIdSource(1)
            .accountType(2)
            .dayBookingInstruction('c')
            .bookingUnit('d')
            .allocationId("string5")
            .numberOfAllocations(2)
            .allocationAccount("string6")
            .allocationAccountIdSource(3)
            .allocationSettlementCurrency(Currency.USD)
            .individualAllocationId("string7")
            .allocatedQuantity(1.0)
            .settlementType('e')
            .settlementDate(today)
            .cashMargin('f')
            .clearingFeeIndicator("string10")
            .handlingInstructions('g')
            .executionInstructions(List.of('h','i','j','k'))
            .minimumQuantity(2.0)
            .maxFloor(3.0)
            .executionDestination(MarketIdentifierCode.ABXX)
            .numberOfTradingSessions(4)
            .tradingSessionSubID("string8")
            .tradingSessionId("string9")
            .processCode('l')
            .numberOfUnderlyingLegs(5)
            .previousClosePrice(4.0)
            .locateRequired(true)
            .quantityType(6)
            .priceType(7)
            .price(5.0)
            .stopPrice(6.0)
            .currency(Currency.USD)
            .complianceId("string11")
            .solicitedFlag(false)
            .indicationOfInterestId("string12")
            .quoteId("string13")
            .timeInForce('m')
            .effectiveTime(fiveMinutesAgo)
            .expireDate(today)
            .expireTime(now)
            .gtBookingInst(8)
            .orderCapacity('n')
            .orderRestrictions(List.of("str1","str2", "str3"))
            .customerOrderCapacity(9)
            .forexRequest(true)
            .settlementCurrency(Currency.USD)
            .bookingType(10)
            .text("string14")
            .encodedTextLength(11)
            .encodedText("string15")
            .settlementDateFuture(today)
            .orderQuantityFuture(12)
            .priceFuture(7.0)
            .positionEffect('o')
            .coveredOrUncovered(13)
            .maxShow(14)
            .targetStrategy(15)
            .targetStrategyParameters("string16")
            .participationRate(8.0)
            .cancellationRights('p')
            .moneyLaunderingStatus('q')
            .registrationId("string17")
            .designation("string18")
            .fixHeader(mock(FixHeader.class))
            .fixTrailer(mock(FixTrailer.class))
            .instrument(mock(Instrument.class))
            .orderQuantityData(mock(OrderQuantityData.class))
            .parties(mock(Parties.class))
            .nestedParties(mock(NestedParties.class))
            .underlyingInstrument(mock(UnderlyingInstrument.class))
            .financingDetails(mock(FinancingDetails.class))
            .spreadOrBenchmarkCurveData(mock(SpreadOrBenchmarkCurveData.class))
            .yieldData(mock(YieldData.class))
            .stipulations(mock(Stipulations.class))
            .pegInstructions(mock(PegInstructions.class))
            .discretionInstructions(mock(DiscretionInstructions.class))
            .commissionData(mock(CommissionData.class))
            .build());
  }

  @Test
  void convert_emptyMap_expectNullPointerExceptionFromMissingRequiredFields() {
    FixMessageContext context = FixMessageContext.builder()
        .processedMessages(Map.of())
        .build();

    assertThatThrownBy(() -> converter.convert(context))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  void convert_unsupportedTags_expectOnlyRequiredTags() {
    FixMessageContext context = FixMessageContext.builder()
        .processedMessages(Map.ofEntries(
            getRawTagEntry(11, "string1"),
            getRawTagEntry(54, "a"),
            getRawTagEntry(60, parseDateTimeToString(now)),
            getRawTagEntry(40, "b"),
            getRawTagEntry(501, null),
            getRawTagEntry(500, "3")
        ))
        .build();

    assertThat(converter.convert(context))
        .usingRecursiveComparison()
        .withStrictTypeChecking()
        .isEqualTo(NewOrderSingle.builder()
            .clordid("string1")
            .side('a')
            .transactTime(now)
            .orderType('b')
            .fixHeader(mock(FixHeader.class))
            .fixTrailer(mock(FixTrailer.class))
            .instrument(mock(Instrument.class))
            .orderQuantityData(mock(OrderQuantityData.class))
            .parties(mock(Parties.class))
            .nestedParties(mock(NestedParties.class))
            .underlyingInstrument(mock(UnderlyingInstrument.class))
            .financingDetails(mock(FinancingDetails.class))
            .spreadOrBenchmarkCurveData(mock(SpreadOrBenchmarkCurveData.class))
            .yieldData(mock(YieldData.class))
            .stipulations(mock(Stipulations.class))
            .pegInstructions(mock(PegInstructions.class))
            .discretionInstructions(mock(DiscretionInstructions.class))
            .commissionData(mock(CommissionData.class))
            .build());
  }

  @ParameterizedTest
  @MethodSource("invalidValues")
  void convert_invalidValues_expectExceptions(Map.Entry<Integer, RawTag> tagEntry,
      Class<Throwable> expectedException) {
    FixMessageContext context = FixMessageContext.builder()
        .processedMessages(Map.ofEntries(tagEntry))
        .build();

    assertThatThrownBy(() -> converter.convert(context))
        .isInstanceOf(expectedException);
  }

  private static Stream<Arguments> invalidValues() {
    return Stream.of(
        Arguments.of(getRawTagEntry(54, ""), StringIndexOutOfBoundsException.class),
        Arguments.of(getRawTagEntry(54, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(60, "5/5/2015"), DateTimeParseException.class),
        Arguments.of(getRawTagEntry(60, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(40, ""), StringIndexOutOfBoundsException.class),
        Arguments.of(getRawTagEntry(40, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(229, "5/5/2015"), DateTimeParseException.class),
        Arguments.of(getRawTagEntry(229, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(75, "5/5/2015"), DateTimeParseException.class),
        Arguments.of(getRawTagEntry(75, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(660, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(660, null), NumberFormatException.class),
        Arguments.of(getRawTagEntry(581, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(581, null), NumberFormatException.class),
        Arguments.of(getRawTagEntry(589, ""), StringIndexOutOfBoundsException.class),
        Arguments.of(getRawTagEntry(589, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(590, ""), StringIndexOutOfBoundsException.class),
        Arguments.of(getRawTagEntry(590, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(78, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(78, null), NumberFormatException.class),
        Arguments.of(getRawTagEntry(661, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(661, null), NumberFormatException.class),
        Arguments.of(getRawTagEntry(736, "ABC_"), IllegalArgumentException.class),
        Arguments.of(getRawTagEntry(736, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(80, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(80, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(63, ""), StringIndexOutOfBoundsException.class),
        Arguments.of(getRawTagEntry(63, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(64, "5/5/2015"), DateTimeParseException.class),
        Arguments.of(getRawTagEntry(64, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(544, ""), StringIndexOutOfBoundsException.class),
        Arguments.of(getRawTagEntry(544, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(21, ""), StringIndexOutOfBoundsException.class),
        Arguments.of(getRawTagEntry(21, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(18, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(110, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(110, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(111, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(111, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(100, "ABC_"), IllegalArgumentException.class),
        Arguments.of(getRawTagEntry(100, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(386, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(386, null), NumberFormatException.class),
        Arguments.of(getRawTagEntry(81, ""), StringIndexOutOfBoundsException.class),
        Arguments.of(getRawTagEntry(81, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(711, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(711, null), NumberFormatException.class),
        Arguments.of(getRawTagEntry(140, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(140, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(114, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(854, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(854, null), NumberFormatException.class),
        Arguments.of(getRawTagEntry(423, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(423, null), NumberFormatException.class),
        Arguments.of(getRawTagEntry(44, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(44, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(99, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(99, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(15, "ABC_"), IllegalArgumentException.class),
        Arguments.of(getRawTagEntry(15, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(377, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(59, ""), StringIndexOutOfBoundsException.class),
        Arguments.of(getRawTagEntry(59, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(168, "5/5/2015"), DateTimeParseException.class),
        Arguments.of(getRawTagEntry(168, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(432, "5/5/2015"), DateTimeParseException.class),
        Arguments.of(getRawTagEntry(432, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(126, "5/5/2015"), DateTimeParseException.class),
        Arguments.of(getRawTagEntry(126, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(427, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(427, null), NumberFormatException.class),
        Arguments.of(getRawTagEntry(528, ""), StringIndexOutOfBoundsException.class),
        Arguments.of(getRawTagEntry(528, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(529, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(582, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(582, null), NumberFormatException.class),
        Arguments.of(getRawTagEntry(121, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(120, "ABC_"), IllegalArgumentException.class),
        Arguments.of(getRawTagEntry(120, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(775, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(775, null), NumberFormatException.class),
        Arguments.of(getRawTagEntry(354, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(354, null), NumberFormatException.class),
        Arguments.of(getRawTagEntry(193, "5/5/2015"), DateTimeParseException.class),
        Arguments.of(getRawTagEntry(193, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(192, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(192, null), NumberFormatException.class),
        Arguments.of(getRawTagEntry(640, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(640, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(77, ""), StringIndexOutOfBoundsException.class),
        Arguments.of(getRawTagEntry(77, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(203, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(203, null), NumberFormatException.class),
        Arguments.of(getRawTagEntry(210, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(210, null), NumberFormatException.class),
        Arguments.of(getRawTagEntry(847, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(847, null), NumberFormatException.class),
        Arguments.of(getRawTagEntry(849, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(849, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(480, ""), StringIndexOutOfBoundsException.class),
        Arguments.of(getRawTagEntry(480, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(481, ""), StringIndexOutOfBoundsException.class),
        Arguments.of(getRawTagEntry(481, null), NullPointerException.class)
    );
  }
}
