package com.honestefforts.fixengine.service.converter.component;

import static com.honestefforts.fixengine.service.converter.TestUtility.getRawTagEntry;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.honestefforts.fixengine.model.message.FixMessageContext;
import com.honestefforts.fixengine.model.message.components.CommissionData;
import com.honestefforts.fixengine.model.message.tags.RawTag;
import com.honestefforts.fixengine.model.universal.Currency;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

public class CommissionDataConverterTest {

  @Test
  void convert_happyPath() {
    FixMessageContext context = FixMessageContext.builder()
        .processedMessages(Map.ofEntries(
            getRawTagEntry(12, "3.2"),
            getRawTagEntry(13, "a"),
            getRawTagEntry(479, "USD"),
            getRawTagEntry(497, "Y")
        ))
        .build();

    assertThat(CommissionDataConverter.convert(context))
        .usingRecursiveComparison()
        .withStrictTypeChecking()
        .isEqualTo(CommissionData.builder()
            .commission(3.2)
            .commissionType('a')
            .commissionCurrency(Currency.USD)
            .fundRenewCommissionWaived(true)
            .build());
  }

  @Test
  void convert_emptyMap_expectEmptyObject() {
    FixMessageContext context = FixMessageContext.builder()
        .processedMessages(Map.of())
        .build();

    assertThat(CommissionDataConverter.convert(context))
        .usingRecursiveComparison()
        .withStrictTypeChecking()
        .isEqualTo(CommissionData.builder().build());
  }

  @Test
  void convert_unsupportedTags_expectEmptyObject() {
    FixMessageContext context = FixMessageContext.builder()
        .processedMessages(Map.ofEntries(
            getRawTagEntry(35, "8"),
            getRawTagEntry(8, null)
        ))
        .build();

    assertThat(CommissionDataConverter.convert(context))
        .usingRecursiveComparison()
        .withStrictTypeChecking()
        .isEqualTo(CommissionData.builder().build());
  }

  @ParameterizedTest
  @ValueSource(ints = {12, 13, 479, 497})
  void convert_nullValues_expectExceptions(int tag) {
    FixMessageContext context = FixMessageContext.builder()
        .processedMessages(Map.ofEntries(
            getRawTagEntry(tag, null)
        ))
        .build();

    assertThatThrownBy(() -> CommissionDataConverter.convert(context))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining(" is null");
  }

  @ParameterizedTest
  @MethodSource("invalidValues")
  void convert_invalidValues_expectExceptions(Map.Entry<Integer, RawTag> tagEntry,
      Class<Throwable> expectedException) {
    FixMessageContext context = FixMessageContext.builder()
        .processedMessages(Map.ofEntries(tagEntry))
        .build();

    assertThatThrownBy(() -> CommissionDataConverter.convert(context))
        .isInstanceOf(expectedException);
  }

  private static Stream<Arguments> invalidValues() {
    return Stream.of(
        Arguments.of(getRawTagEntry(12, "double"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(12, "double"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(13, ""), StringIndexOutOfBoundsException.class),
        Arguments.of(getRawTagEntry(479, "ABC"), IllegalArgumentException.class)
    );
  }

}
