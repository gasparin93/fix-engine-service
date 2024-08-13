package com.honestefforts.fixengine.service.converter.component;

import static com.honestefforts.fixengine.model.message.enums.MessageType.NEW_ORDER_SINGLE;
import static com.honestefforts.fixengine.service.TestUtility.getContext;
import static com.honestefforts.fixengine.service.TestUtility.getRawTagEntry;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.honestefforts.fixengine.model.message.FixMessageContext;
import com.honestefforts.fixengine.model.message.components.UnderlyingStipulations;
import com.honestefforts.fixengine.model.message.tags.RawTag;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class UnderlyingStipulationsConverterTest {

  @Test
  void convert_happyPath() {
    FixMessageContext context = getContext(Map.ofEntries(
        getRawTagEntry(887, "1"),
        getRawTagEntry(888, "string1"),
        getRawTagEntry(889, "string2")
    ));

    assertThat(UnderlyingStipulationsConverter.convert(context))
        .usingRecursiveComparison()
        .withStrictTypeChecking()
        .isEqualTo(UnderlyingStipulations.builder()
            .numberOfUnderlyingStipulations(1)
            .underlyingStipulationType("string1")
            .underlyingStipulationValue("string2")
            .build());
  }

  @Test
  void convert_emptyMap_expectEmptyObject() {
    FixMessageContext context = getContext(NEW_ORDER_SINGLE);

    assertThat(UnderlyingStipulationsConverter.convert(context))
        .usingRecursiveComparison()
        .withStrictTypeChecking()
        .isEqualTo(UnderlyingStipulations.builder().build());
  }

  @Test
  void convert_unsupportedTags_expectEmptyObject() {
    FixMessageContext context = getContext(Map.ofEntries(
        getRawTagEntry(35, "8"),
        getRawTagEntry(8, null)
    ));

    assertThat(UnderlyingStipulationsConverter.convert(context))
        .usingRecursiveComparison()
        .withStrictTypeChecking()
        .isEqualTo(UnderlyingStipulations.builder().build());
  }

  @ParameterizedTest
  @MethodSource("invalidValues")
  void convert_invalidValues_expectExceptions(Map.Entry<Integer, RawTag> tagEntry,
      Class<Throwable> expectedException) {
    FixMessageContext context = getContext(Map.ofEntries(tagEntry));

    assertThatThrownBy(() -> UnderlyingStipulationsConverter.convert(context))
        .isInstanceOf(expectedException);
  }

  private static Stream<Arguments> invalidValues() {
    return Stream.of(
        Arguments.of(getRawTagEntry(887, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(887, null), NumberFormatException.class)
    );
  }
}
