package com.honestefforts.fixengine.service.converter.component;

import static com.honestefforts.fixengine.service.TestUtility.getRawTagEntry;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.honestefforts.fixengine.model.message.FixMessageContext;
import com.honestefforts.fixengine.model.message.components.SpreadOrBenchmarkCurveData;
import com.honestefforts.fixengine.model.message.tags.RawTag;
import com.honestefforts.fixengine.model.universal.Currency;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class SpreadOrBenchmarkCurveDataConverterTest {

  @Test
  void convert_happyPath() {
    FixMessageContext context = FixMessageContext.builder()
        .processedMessages(Map.ofEntries(
            getRawTagEntry(218, "1.0"),
            getRawTagEntry(220, Currency.USD.name()),
            getRawTagEntry(221, "string1"),
            getRawTagEntry(222, "string2"),
            getRawTagEntry(662, "2.0"),
            getRawTagEntry(663, "1"),
            getRawTagEntry(699, "string3"),
            getRawTagEntry(761, "string4")
        ))
        .build();

    assertThat(SpreadOrBenchmarkCurveDataConverter.convert(context))
        .usingRecursiveComparison()
        .withStrictTypeChecking()
        .isEqualTo(SpreadOrBenchmarkCurveData.builder()
            .spread(1.0)
            .benchmarkCurveCurrency(Currency.USD)
            .benchmarkCurveName("string1")
            .benchmarkCurvePoint("string2")
            .benchmarkPrice(2.0)
            .benchmarkPriceType(1)
            .benchmarkSecurityId("string3")
            .benchmarkSecurityIdSource("string4")
            .build());
  }

  @Test
  void convert_emptyMap_expectEmptyObject() {
    FixMessageContext context = FixMessageContext.builder()
        .processedMessages(Map.of())
        .build();

    assertThat(SpreadOrBenchmarkCurveDataConverter.convert(context))
        .usingRecursiveComparison()
        .withStrictTypeChecking()
        .isEqualTo(SpreadOrBenchmarkCurveData.builder().build());
  }

  @Test
  void convert_unsupportedTags_expectEmptyObject() {
    FixMessageContext context = FixMessageContext.builder()
        .processedMessages(Map.ofEntries(
            getRawTagEntry(35, "8"),
            getRawTagEntry(8, null)
        ))
        .build();

    assertThat(SpreadOrBenchmarkCurveDataConverter.convert(context))
        .usingRecursiveComparison()
        .withStrictTypeChecking()
        .isEqualTo(SpreadOrBenchmarkCurveData.builder().build());
  }

  @ParameterizedTest
  @MethodSource("invalidValues")
  void convert_invalidValues_expectExceptions(Map.Entry<Integer, RawTag> tagEntry,
      Class<Throwable> expectedException) {
    FixMessageContext context = FixMessageContext.builder()
        .processedMessages(Map.ofEntries(tagEntry))
        .build();

    assertThatThrownBy(() -> SpreadOrBenchmarkCurveDataConverter.convert(context))
        .isInstanceOf(expectedException);
  }

  private static Stream<Arguments> invalidValues() {
    return Stream.of(
        Arguments.of(getRawTagEntry(218, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(218, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(220, "ABC"), IllegalArgumentException.class),
        Arguments.of(getRawTagEntry(220, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(662, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(662, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(663, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(663, null), NumberFormatException.class)
    );
  }
}
