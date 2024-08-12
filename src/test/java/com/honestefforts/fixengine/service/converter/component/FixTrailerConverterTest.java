package com.honestefforts.fixengine.service.converter.component;

import static com.honestefforts.fixengine.service.TestUtility.getContext;
import static com.honestefforts.fixengine.service.TestUtility.getRawTagEntry;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.honestefforts.fixengine.model.message.FixMessageContext;
import com.honestefforts.fixengine.model.message.components.FixTrailer;
import com.honestefforts.fixengine.model.message.tags.RawTag;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class FixTrailerConverterTest {

  @Test
  void convert_happyPath() {
    FixMessageContext context = getContext(Map.ofEntries(
        getRawTagEntry(10, "string1"),
        getRawTagEntry(93, "1"),
        getRawTagEntry(89, "string2")
    ));

    assertThat(FixTrailerConverter.convert(context))
        .usingRecursiveComparison()
        .withStrictTypeChecking()
        .isEqualTo(FixTrailer.builder()
            .checkSum("string1")
            .signatureLength(1)
            .signature("string2")
            .build());
  }

  @Test
  void convert_emptyMap_expectNullPointerExceptionFromMissingRequiredFields() {
    FixMessageContext context = getContext("D");

    assertThatThrownBy(() -> FixHeaderConverter.convert(context))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  void convert_unsupportedTags_expectOnlyRequiredTags() {
    FixMessageContext context = getContext(Map.ofEntries(
        getRawTagEntry(10, "string1"),
        getRawTagEntry(35, "8"),
        getRawTagEntry(8, null)
    ));

    assertThat(FixTrailerConverter.convert(context))
        .usingRecursiveComparison()
        .withStrictTypeChecking()
        .isEqualTo(FixTrailer.builder()
            .checkSum("string1")
            .build());
  }

  @ParameterizedTest
  @MethodSource("invalidValues")
  void convert_invalidValues_expectExceptions(Map.Entry<Integer, RawTag> tagEntry,
      Class<Throwable> expectedException) {
    FixMessageContext context = getContext(Map.ofEntries(tagEntry));

    assertThatThrownBy(() -> FixTrailerConverter.convert(context))
        .isInstanceOf(expectedException);
  }

  private static Stream<Arguments> invalidValues() {
    return Stream.of(
        Arguments.of(getRawTagEntry(10, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(93, "string"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(93, null), NumberFormatException.class)
    );
  }
}
