package com.honestefforts.fixengine.service.converter.component;

import static com.honestefforts.fixengine.service.TestUtility.getContext;
import static com.honestefforts.fixengine.service.TestUtility.getRawTagEntry;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.honestefforts.fixengine.model.message.FixMessageContext;
import com.honestefforts.fixengine.model.message.components.PegInstructions;
import com.honestefforts.fixengine.model.message.tags.RawTag;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class PegInstructionsConverterTest {

  @Test
  void convert_happyPath() {
    FixMessageContext context = getContext(Map.ofEntries(
        getRawTagEntry(211, "1.0"),
        getRawTagEntry(835, "1"),
        getRawTagEntry(836, "2"),
        getRawTagEntry(837, "3"),
        getRawTagEntry(838, "4"),
        getRawTagEntry(840, "5")
    ));

    assertThat(PegInstructionsConverter.convert(context))
        .usingRecursiveComparison()
        .withStrictTypeChecking()
        .isEqualTo(PegInstructions.builder()
            .pegOffsetValue(1.0)
            .pegMoveType(1)
            .pegOffsetType(2)
            .pegLimitType(3)
            .pegRoundDirection(4)
            .pegScope(5)
            .build());
  }

  @Test
  void convert_emptyMap_expectEmptyObject() {
    FixMessageContext context = getContext("D");

    assertThat(PegInstructionsConverter.convert(context))
        .usingRecursiveComparison()
        .withStrictTypeChecking()
        .isEqualTo(PegInstructions.builder().build());
  }

  @Test
  void convert_unsupportedTags_expectEmptyObject() {
    FixMessageContext context = getContext(Map.ofEntries(
        getRawTagEntry(35, "8"),
        getRawTagEntry(8, null)
    ));

    assertThat(PegInstructionsConverter.convert(context))
        .usingRecursiveComparison()
        .withStrictTypeChecking()
        .isEqualTo(PegInstructions.builder().build());
  }

  @ParameterizedTest
  @MethodSource("invalidValues")
  void convert_invalidValues_expectExceptions(Map.Entry<Integer, RawTag> tagEntry,
      Class<Throwable> expectedException) {
    FixMessageContext context = getContext(Map.ofEntries(tagEntry));

    assertThatThrownBy(() -> PegInstructionsConverter.convert(context))
        .isInstanceOf(expectedException);
  }

  private static Stream<Arguments> invalidValues() {
    return Stream.of(
        Arguments.of(getRawTagEntry(211, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(211, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(835, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(835, null), NumberFormatException.class),
        Arguments.of(getRawTagEntry(836, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(836, null), NumberFormatException.class),
        Arguments.of(getRawTagEntry(837, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(837, null), NumberFormatException.class),
        Arguments.of(getRawTagEntry(838, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(838, null), NumberFormatException.class),
        Arguments.of(getRawTagEntry(840, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(840, null), NumberFormatException.class)
    );
  }
}
