package com.honestefforts.fixengine.service.converter.component;

import static com.honestefforts.fixengine.service.converter.TestUtility.getRawTagEntry;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.honestefforts.fixengine.model.message.FixMessageContext;
import com.honestefforts.fixengine.model.message.components.Stipulations;
import com.honestefforts.fixengine.model.message.tags.RawTag;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class StipulationsConverterTest {

  @Test
  void convert_happyPath() {
    FixMessageContext context = FixMessageContext.builder()
        .processedMessages(Map.ofEntries(
            getRawTagEntry(232, "1"),
            getRawTagEntry(233, "string1"),
            getRawTagEntry(234, "string2")
        ))
        .build();

    assertThat(StipulationsConverter.convert(context))
        .usingRecursiveComparison()
        .withStrictTypeChecking()
        .isEqualTo(Stipulations.builder()
            .numberOfStipulations(1)
            .stipulationType("string1")
            .stipulationValue("string2")
            .build());
  }

  @Test
  void convert_emptyMap_expectEmptyObject() {
    FixMessageContext context = FixMessageContext.builder()
        .processedMessages(Map.of())
        .build();

    assertThat(StipulationsConverter.convert(context))
        .usingRecursiveComparison()
        .withStrictTypeChecking()
        .isEqualTo(Stipulations.builder().build());
  }

  @Test
  void convert_unsupportedTags_expectEmptyObject() {
    FixMessageContext context = FixMessageContext.builder()
        .processedMessages(Map.ofEntries(
            getRawTagEntry(35, "8"),
            getRawTagEntry(8, null)
        ))
        .build();

    assertThat(StipulationsConverter.convert(context))
        .usingRecursiveComparison()
        .withStrictTypeChecking()
        .isEqualTo(Stipulations.builder().build());
  }

  @ParameterizedTest
  @MethodSource("invalidValues")
  void convert_invalidValues_expectExceptions(Map.Entry<Integer, RawTag> tagEntry,
      Class<Throwable> expectedException) {
    FixMessageContext context = FixMessageContext.builder()
        .processedMessages(Map.ofEntries(tagEntry))
        .build();

    assertThatThrownBy(() -> StipulationsConverter.convert(context))
        .isInstanceOf(expectedException);
  }

  private static Stream<Arguments> invalidValues() {
    return Stream.of(
        Arguments.of(getRawTagEntry(232, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(232, null), NumberFormatException.class)
    );
  }
}
