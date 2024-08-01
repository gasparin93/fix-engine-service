package com.honestefforts.fixengine.service.converter.component;

import static com.honestefforts.fixengine.service.TestUtility.getRawTagEntry;
import static com.honestefforts.fixengine.service.TestUtility.parseDateToString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.honestefforts.fixengine.model.message.FixMessageContext;
import com.honestefforts.fixengine.model.message.components.YieldData;
import com.honestefforts.fixengine.model.message.tags.RawTag;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class YieldDataConverterTest {

  @Test
  void convert_happyPath() {
    LocalDate today = LocalDate.now();
    LocalDate tomorrow = today.plusDays(1);
    FixMessageContext context = FixMessageContext.builder()
        .processedMessages(Map.ofEntries(
            getRawTagEntry(235, "string1"),
            getRawTagEntry(236, "1.0"),
            getRawTagEntry(701, parseDateToString(today)),
            getRawTagEntry(696, parseDateToString(tomorrow)),
            getRawTagEntry(697, "2.0"),
            getRawTagEntry(698, "1")
        ))
        .build();

    assertThat(YieldDataConverter.convert(context))
        .usingRecursiveComparison()
        .withStrictTypeChecking()
        .isEqualTo(YieldData.builder()
            .yieldType("string1")
            .yield(1.0)
            .yieldCalcDate(today)
            .yieldRedemptionDate(tomorrow)
            .yieldRedemptionPrice(2.0)
            .yieldRedemptionPriceType(1)
            .build());
  }

  @Test
  void convert_emptyMap_expectEmptyObject() {
    FixMessageContext context = FixMessageContext.builder()
        .processedMessages(Map.of())
        .build();

    assertThat(YieldDataConverter.convert(context))
        .usingRecursiveComparison()
        .withStrictTypeChecking()
        .isEqualTo(YieldData.builder().build());
  }

  @Test
  void convert_unsupportedTags_expectEmptyObject() {
    FixMessageContext context = FixMessageContext.builder()
        .processedMessages(Map.ofEntries(
            getRawTagEntry(35, "8"),
            getRawTagEntry(8, null)
        ))
        .build();

    assertThat(YieldDataConverter.convert(context))
        .usingRecursiveComparison()
        .withStrictTypeChecking()
        .isEqualTo(YieldData.builder().build());
  }

  @ParameterizedTest
  @MethodSource("invalidValues")
  void convert_invalidValues_expectExceptions(Map.Entry<Integer, RawTag> tagEntry,
      Class<Throwable> expectedException) {
    FixMessageContext context = FixMessageContext.builder()
        .processedMessages(Map.ofEntries(tagEntry))
        .build();

    assertThatThrownBy(() -> YieldDataConverter.convert(context))
        .isInstanceOf(expectedException);
  }

  private static Stream<Arguments> invalidValues() {
    return Stream.of(
        Arguments.of(getRawTagEntry(236, "number"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(236, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(701, "5/5/2015"), DateTimeParseException.class),
        Arguments.of(getRawTagEntry(701, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(696, "5/5/2015"), DateTimeParseException.class),
        Arguments.of(getRawTagEntry(696, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(697, "number"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(697, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(698, "number"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(698, "number"), NumberFormatException.class)
    );
  }
}
