package com.honestefforts.fixengine.service.converter.component;

import static com.honestefforts.fixengine.service.TestUtility.getContext;
import static com.honestefforts.fixengine.service.TestUtility.getRawTagEntry;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.honestefforts.fixengine.model.message.FixMessageContext;
import com.honestefforts.fixengine.model.message.components.Parties;
import com.honestefforts.fixengine.model.message.tags.RawTag;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class PartiesConverterTest {
  
  @Test
  void convert_happyPath() {
    FixMessageContext context = getContext(Map.ofEntries(
        getRawTagEntry(453, "1"),
        getRawTagEntry(448, "string1"),
        getRawTagEntry(447, "a"),
        getRawTagEntry(452, "2"),
        getRawTagEntry(802, "3"),
        getRawTagEntry(523, "string2"),
        getRawTagEntry(803, "4")
    ));

    assertThat(PartiesConverter.convert(context))
        .usingRecursiveComparison()
        .withStrictTypeChecking()
        .isEqualTo(Parties.builder()
            .numberOfPartyIds(1)
            .partyId("string1")
            .partyIdSource('a')
            .partyRole(2)
            .numberOfPartySubIds(3)
            .partySubId("string2")
            .partySubIdType(4)
            .build());
  }

  @Test
  void convert_emptyMap_expectEmptyObject() {
    FixMessageContext context = getContext("D");
    assertThat(PartiesConverter.convert(context))
        .usingRecursiveComparison()
        .withStrictTypeChecking()
        .isEqualTo(Parties.builder().build());
  }

  @Test
  void convert_unsupportedTags_expectEmptyObject() {
    FixMessageContext context = getContext(Map.ofEntries(
        getRawTagEntry(35, "8"),
        getRawTagEntry(8, null)
    ));

    assertThat(PartiesConverter.convert(context))
        .usingRecursiveComparison()
        .withStrictTypeChecking()
        .isEqualTo(Parties.builder().build());
  }

  @ParameterizedTest
  @MethodSource("invalidValues")
  void convert_invalidValues_expectExceptions(Map.Entry<Integer, RawTag> tagEntry,
      Class<Throwable> expectedException) {
    FixMessageContext context = getContext(Map.ofEntries(tagEntry));

    assertThatThrownBy(() -> PartiesConverter.convert(context))
        .isInstanceOf(expectedException);
  }

  private static Stream<Arguments> invalidValues() {
    return Stream.of(
        Arguments.of(getRawTagEntry(453, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(453, null), NumberFormatException.class),
        Arguments.of(getRawTagEntry(447, ""), StringIndexOutOfBoundsException.class),
        Arguments.of(getRawTagEntry(447, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(452, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(452, null), NumberFormatException.class),
        Arguments.of(getRawTagEntry(802, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(802, null), NumberFormatException.class),
        Arguments.of(getRawTagEntry(803, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(803, null), NumberFormatException.class)
    );
  }
}
