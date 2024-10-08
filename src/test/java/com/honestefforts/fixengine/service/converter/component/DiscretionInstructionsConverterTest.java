package com.honestefforts.fixengine.service.converter.component;

import static com.honestefforts.fixengine.model.message.enums.MessageType.NEW_ORDER_SINGLE;
import static com.honestefforts.fixengine.service.TestUtility.getContext;
import static com.honestefforts.fixengine.service.TestUtility.getRawTagEntry;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.honestefforts.fixengine.model.message.FixMessageContext;
import com.honestefforts.fixengine.model.message.components.DiscretionInstructions;
import com.honestefforts.fixengine.model.message.tags.RawTag;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class DiscretionInstructionsConverterTest {

  @Test
  void convert_happyPath() {
    FixMessageContext context = getContext(Map.ofEntries(
        getRawTagEntry(388, "a"),
        getRawTagEntry(389, "3.2"),
        getRawTagEntry(841, "1"),
        getRawTagEntry(842, "2"),
        getRawTagEntry(843, "3"),
        getRawTagEntry(844, "4"),
        getRawTagEntry(846, "5")
    ));

    assertThat(DiscretionInstructionsConverter.convert(context))
        .usingRecursiveComparison()
        .withStrictTypeChecking()
        .isEqualTo(DiscretionInstructions.builder()
            .discretionInstruction('a')
            .discretionOffsetValue(3.2)
            .discretionMoveType(1)
            .discretionOffsetType(2)
            .discretionLimitType(3)
            .discretionRoundDirection(4)
            .discretionScope(5)
            .build());
  }

  @Test
  void convert_emptyMap_expectEmptyObject() {
    FixMessageContext context = getContext(NEW_ORDER_SINGLE);

    assertThat(DiscretionInstructionsConverter.convert(context))
        .usingRecursiveComparison()
        .withStrictTypeChecking()
        .isEqualTo(DiscretionInstructions.builder().build());
  }

  @Test
  void convert_unsupportedTags_expectEmptyObject() {
    FixMessageContext context = getContext(Map.ofEntries(
        getRawTagEntry(35, "8"),
        getRawTagEntry(8, null)
    ));

    assertThat(DiscretionInstructionsConverter.convert(context))
        .usingRecursiveComparison()
        .withStrictTypeChecking()
        .isEqualTo(DiscretionInstructions.builder().build());
  }

  @ParameterizedTest
  @MethodSource("invalidValues")
  void convert_invalidValues_expectExceptions(Map.Entry<Integer, RawTag> tagEntry,
      Class<Throwable> expectedException) {
    FixMessageContext context = getContext(NEW_ORDER_SINGLE, Map.ofEntries(tagEntry));

    assertThatThrownBy(() -> DiscretionInstructionsConverter.convert(context))
        .isInstanceOf(expectedException);
  }

  private static Stream<Arguments> invalidValues() {
    return Stream.of(
        Arguments.of(getRawTagEntry(388, ""), StringIndexOutOfBoundsException.class),
        Arguments.of(getRawTagEntry(388, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(389, "double"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(389, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(841, "a number"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(841, null), NumberFormatException.class),
        Arguments.of(getRawTagEntry(842, "a number"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(842, null), NumberFormatException.class),
        Arguments.of(getRawTagEntry(843, "a number"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(843, null), NumberFormatException.class),
        Arguments.of(getRawTagEntry(844, "a number"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(844, null), NumberFormatException.class),
        Arguments.of(getRawTagEntry(846, "a number"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(846, null), NumberFormatException.class)
    );
  }
}
