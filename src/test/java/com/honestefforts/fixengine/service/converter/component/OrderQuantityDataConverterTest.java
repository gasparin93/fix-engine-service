package com.honestefforts.fixengine.service.converter.component;

import static com.honestefforts.fixengine.service.converter.TestUtility.getRawTagEntry;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.honestefforts.fixengine.model.message.FixMessageContext;
import com.honestefforts.fixengine.model.message.components.OrderQuantityData;
import com.honestefforts.fixengine.model.message.tags.RawTag;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class OrderQuantityDataConverterTest {

  @Test
  void convert_happyPath() {
    FixMessageContext context = FixMessageContext.builder()
        .processedMessages(Map.ofEntries(
            getRawTagEntry(38, "1"),
            getRawTagEntry(152, "2"),
            getRawTagEntry(516, "1.0"),
            getRawTagEntry(468, "a"),
            getRawTagEntry(469, "2.0")
        ))
        .build();

    assertThat(OrderQuantityDataConverter.convert(context))
        .usingRecursiveComparison()
        .withStrictTypeChecking()
        .isEqualTo(OrderQuantityData.builder()
            .orderQuantity(1)
            .cashOrderQuantity(2)
            .orderPercent(1.0)
            .roundingDirection('a')
            .roundingModulus(2.0)
            .build());
  }

  @Test
  void convert_emptyMap_expectEmptyObject() {
    FixMessageContext context = FixMessageContext.builder()
        .processedMessages(Map.of())
        .build();

    assertThat(OrderQuantityDataConverter.convert(context))
        .usingRecursiveComparison()
        .withStrictTypeChecking()
        .isEqualTo(OrderQuantityData.builder().build());
  }

  @Test
  void convert_unsupportedTags_expectEmptyObject() {
    FixMessageContext context = FixMessageContext.builder()
        .processedMessages(Map.ofEntries(
            getRawTagEntry(35, "8"),
            getRawTagEntry(8, null)
        ))
        .build();

    assertThat(OrderQuantityDataConverter.convert(context))
        .usingRecursiveComparison()
        .withStrictTypeChecking()
        .isEqualTo(OrderQuantityData.builder().build());
  }

  @ParameterizedTest
  @MethodSource("invalidValues")
  void convert_invalidValues_expectExceptions(Map.Entry<Integer, RawTag> tagEntry,
      Class<Throwable> expectedException) {
    FixMessageContext context = FixMessageContext.builder()
        .processedMessages(Map.ofEntries(tagEntry))
        .build();

    assertThatThrownBy(() -> OrderQuantityDataConverter.convert(context))
        .isInstanceOf(expectedException);
  }

  private static Stream<Arguments> invalidValues() {
    return Stream.of(
        Arguments.of(getRawTagEntry(38, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(38, null), NumberFormatException.class),
        Arguments.of(getRawTagEntry(152, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(152, null), NumberFormatException.class),
        Arguments.of(getRawTagEntry(516, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(516, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(468, ""), StringIndexOutOfBoundsException.class),
        Arguments.of(getRawTagEntry(468, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(469, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(469, null), NullPointerException.class)
    );
  }
}
