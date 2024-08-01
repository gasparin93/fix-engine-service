package com.honestefforts.fixengine.service.converter.component;

import static com.honestefforts.fixengine.service.TestUtility.getRawTagEntry;
import static com.honestefforts.fixengine.service.TestUtility.parseDateToString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.honestefforts.fixengine.model.message.FixMessageContext;
import com.honestefforts.fixengine.model.message.components.FinancingDetails;
import com.honestefforts.fixengine.model.message.tags.RawTag;
import com.honestefforts.fixengine.model.universal.Currency;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class FinancingDetailsConverterTest {

  @Test
  void convert_happyPath() {
    LocalDate today = LocalDate.now();
    LocalDate tomorrow = today.plusDays(1);
    LocalDate nextWeek = today.plusWeeks(1);
    FixMessageContext context = FixMessageContext.builder()
        .processedMessages(Map.ofEntries(
            getRawTagEntry(913, "string"),
            getRawTagEntry(914, "string"),
            getRawTagEntry(915, parseDateToString(today)),
            getRawTagEntry(918, Currency.USD.name()),
            getRawTagEntry(788, "1"),
            getRawTagEntry(916, parseDateToString(tomorrow)),
            getRawTagEntry(917, parseDateToString(nextWeek)),
            getRawTagEntry(919, "2"),
            getRawTagEntry(898, "3.2")
        ))
        .build();

    assertThat(FinancingDetailsConverter.convert(context))
        .usingRecursiveComparison()
        .withStrictTypeChecking()
        .isEqualTo(FinancingDetails.builder()
            .agreementDesc("string")
            .agreementId("string")
            .agreementDate(today)
            .agreementCurrency(Currency.USD)
            .terminationType(1)
            .startDate(tomorrow)
            .endDate(nextWeek)
            .deliveryType(2)
            .marginRatio(3.2)
            .build());
  }

  @Test
  void convert_emptyMap_expectEmptyObject() {
    FixMessageContext context = FixMessageContext.builder()
        .processedMessages(Map.of())
        .build();

    assertThat(FinancingDetailsConverter.convert(context))
        .usingRecursiveComparison()
        .withStrictTypeChecking()
        .isEqualTo(FinancingDetails.builder().build());
  }

  @Test
  void convert_unsupportedTags_expectEmptyObject() {
    FixMessageContext context = FixMessageContext.builder()
        .processedMessages(Map.ofEntries(
            getRawTagEntry(35, "8"),
            getRawTagEntry(8, null)
        ))
        .build();

    assertThat(FinancingDetailsConverter.convert(context))
        .usingRecursiveComparison()
        .withStrictTypeChecking()
        .isEqualTo(FinancingDetails.builder().build());
  }

  @ParameterizedTest
  @MethodSource("invalidValues")
  void convert_invalidValues_expectExceptions(Map.Entry<Integer, RawTag> tagEntry,
      Class<Throwable> expectedException) {
    FixMessageContext context = FixMessageContext.builder()
        .processedMessages(Map.ofEntries(tagEntry))
        .build();

    assertThatThrownBy(() -> FinancingDetailsConverter.convert(context))
        .isInstanceOf(expectedException);
  }

  private static Stream<Arguments> invalidValues() {
    return Stream.of(
        Arguments.of(getRawTagEntry(915, ""), DateTimeParseException.class),
        Arguments.of(getRawTagEntry(915, "5/5/2015"), DateTimeParseException.class),
        Arguments.of(getRawTagEntry(915, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(918, "ABC"), IllegalArgumentException.class),
        Arguments.of(getRawTagEntry(918, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(788, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(788, null), NumberFormatException.class),
        Arguments.of(getRawTagEntry(916, ""), DateTimeParseException.class),
        Arguments.of(getRawTagEntry(916, "5/5/2015"), DateTimeParseException.class),
        Arguments.of(getRawTagEntry(916, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(917, ""), DateTimeParseException.class),
        Arguments.of(getRawTagEntry(917, "5/5/2015"), DateTimeParseException.class),
        Arguments.of(getRawTagEntry(917, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(919, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(919, null), NumberFormatException.class),
        Arguments.of(getRawTagEntry(898, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(898, null), NullPointerException.class)
    );
  }

}
