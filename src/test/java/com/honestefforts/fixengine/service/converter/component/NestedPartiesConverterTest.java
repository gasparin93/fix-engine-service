package com.honestefforts.fixengine.service.converter.component;

import static com.honestefforts.fixengine.service.TestUtility.getRawTagEntry;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.honestefforts.fixengine.model.message.FixMessageContext;
import com.honestefforts.fixengine.model.message.components.NestedParties;
import com.honestefforts.fixengine.model.message.tags.RawTag;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class NestedPartiesConverterTest {

  @Test
  void convert_happyPath() {
    FixMessageContext context = FixMessageContext.builder()
        .processedMessages(Map.ofEntries(
            getRawTagEntry(539, "1"),
            getRawTagEntry(524, "string1"),
            getRawTagEntry(525, "a"),
            getRawTagEntry(538, "2"),
            getRawTagEntry(804, "3"),
            getRawTagEntry(545, "string2"),
            getRawTagEntry(805, "4")
        ))
        .build();

    assertThat(NestedPartiesConverter.convert(context))
        .usingRecursiveComparison()
        .withStrictTypeChecking()
        .isEqualTo(NestedParties.builder()
            .numberOfNestedPartyIds(1)
            .nestedPartyId("string1")
            .nestedPartyIdSource('a')
            .nestedPartyRole(2)
            .numberOfNestedPartySubIds(3)
            .nestedPartySubId("string2")
            .nestedPartySubIdType(4)
            .build());
  }

  @Test
  void convert_emptyMap_expectEmptyObject() {
    FixMessageContext context = FixMessageContext.builder()
        .processedMessages(Map.of())
        .build();

    assertThat(NestedPartiesConverter.convert(context))
        .usingRecursiveComparison()
        .withStrictTypeChecking()
        .isEqualTo(NestedParties.builder().build());
  }

  @Test
  void convert_unsupportedTags_expectEmptyObject() {
    FixMessageContext context = FixMessageContext.builder()
        .processedMessages(Map.ofEntries(
            getRawTagEntry(35, "8"),
            getRawTagEntry(8, null)
        ))
        .build();

    assertThat(NestedPartiesConverter.convert(context))
        .usingRecursiveComparison()
        .withStrictTypeChecking()
        .isEqualTo(NestedParties.builder().build());
  }

  @ParameterizedTest
  @MethodSource("invalidValues")
  void convert_invalidValues_expectExceptions(Map.Entry<Integer, RawTag> tagEntry,
      Class<Throwable> expectedException) {
    FixMessageContext context = FixMessageContext.builder()
        .processedMessages(Map.ofEntries(tagEntry))
        .build();

    assertThatThrownBy(() -> NestedPartiesConverter.convert(context))
        .isInstanceOf(expectedException);
  }

  private static Stream<Arguments> invalidValues() {
    return Stream.of(
        Arguments.of(getRawTagEntry(539, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(539, null), NumberFormatException.class),
        Arguments.of(getRawTagEntry(525, ""), StringIndexOutOfBoundsException.class),
        Arguments.of(getRawTagEntry(525, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(538, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(538, null), NumberFormatException.class),
        Arguments.of(getRawTagEntry(804, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(804, null), NumberFormatException.class),
        Arguments.of(getRawTagEntry(805, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(805, null), NumberFormatException.class)
    );
  }
}
