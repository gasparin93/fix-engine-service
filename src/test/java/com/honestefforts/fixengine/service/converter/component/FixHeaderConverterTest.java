package com.honestefforts.fixengine.service.converter.component;

import static com.honestefforts.fixengine.service.TestUtility.getContext;
import static com.honestefforts.fixengine.service.TestUtility.getRawTagEntry;
import static com.honestefforts.fixengine.service.TestUtility.parseDateTimeMsToString;
import static com.honestefforts.fixengine.service.TestUtility.parseDateTimeToString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.honestefforts.fixengine.model.message.FixMessageContext;
import com.honestefforts.fixengine.model.message.components.FixHeader;
import com.honestefforts.fixengine.model.message.tags.RawTag;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class FixHeaderConverterTest {

  private static final LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
  private static final LocalDateTime anHourFromNow = now.plusHours(1).truncatedTo(ChronoUnit.MILLIS);
  private static final LocalDateTime fiveMinutesAgo = now.minusMinutes(5).truncatedTo(ChronoUnit.SECONDS);

  @Test
  void convert_happyPath() {
    FixMessageContext context = getContext(Map.ofEntries(
        getRawTagEntry(8, "string1"),
        getRawTagEntry(9, "1"),
        getRawTagEntry(35, "string2"),
        getRawTagEntry(34, "2"),
        getRawTagEntry(52, parseDateTimeToString(now)),
        getRawTagEntry(49, "string3"),
        getRawTagEntry(56, "string4"),
        getRawTagEntry(115, "string5"),
        getRawTagEntry(128, "string6"),
        getRawTagEntry(90, "3"),
        getRawTagEntry(91, "string7"),
        getRawTagEntry(50, "string8"),
        getRawTagEntry(142, "string9"),
        getRawTagEntry(57, "string10"),
        getRawTagEntry(143, "string11"),
        getRawTagEntry(116, "string12"),
        getRawTagEntry(144, "string13"),
        getRawTagEntry(129, "string14"),
        getRawTagEntry(145, "string15"),
        getRawTagEntry(43, "Y"),
        getRawTagEntry(97, "N"),
        getRawTagEntry(122, parseDateTimeMsToString(anHourFromNow)),
        getRawTagEntry(212, "4"),
        getRawTagEntry(213, "string16"),
        getRawTagEntry(347, "string17"),
        getRawTagEntry(369, "5"),
        getRawTagEntry(627, "6"),
        getRawTagEntry(628, "string18"),
        getRawTagEntry(629, parseDateTimeToString(fiveMinutesAgo)),
        getRawTagEntry(630, "string19")
    ));

    assertThat(FixHeaderConverter.convert(context))
        .usingRecursiveComparison()
        .withStrictTypeChecking()
        .isEqualTo(FixHeader.builder()
            .version("string1")
            .bodyLength(1)
            .messageType("string2")
            .msgSeqNum(2)
            .sendingTime(now)
            .senderCompID("string3")
            .targetCompID("string4")
            .onBehalfOfCompID("string5")
            .deliverToCompID("string6")
            .secureDataLength(3)
            .secureData("string7")
            .senderSubId("string8")
            .senderLocationId("string9")
            .targetSubId("string10")
            .targetLocationId("string11")
            .onBehalfOfSubId("string12")
            .onBehalfOfLocationId("string13")
            .deliverToSubId("string14")
            .deliverToLocationId("string15")
            .possibleDuplicationFlag(true)
            .possibleResend(false)
            .originalSendingTime(anHourFromNow)
            .xmlDataLength(4)
            .xmlData("string16")
            .messageEncoding("string17")
            .lastMsgSeqNumProcessed(5)
            .numberOfHops(6)
            .hopCompId("string18")
            .hopSendingTime(fiveMinutesAgo)
            .hopRefId("string19")
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
        getRawTagEntry(8, "string1"),
        getRawTagEntry(9, "1"),
        getRawTagEntry(35, "string2"),
        getRawTagEntry(34, "2"),
        getRawTagEntry(52, parseDateTimeToString(now)),
        getRawTagEntry(49, "string3"),
        getRawTagEntry(56, "string4"),
        getRawTagEntry(1, "8"),
        getRawTagEntry(2, null),
        getRawTagEntry(3, "3")
    ));

    assertThat(FixHeaderConverter.convert(context))
        .usingRecursiveComparison()
        .withStrictTypeChecking()
        .isEqualTo(FixHeader.builder()
            .version("string1")
            .bodyLength(1)
            .messageType("string2")
            .msgSeqNum(2)
            .sendingTime(now)
            .senderCompID("string3")
            .targetCompID("string4")
            .build());
  }

  @ParameterizedTest
  @MethodSource("invalidValues")
  void convert_invalidValues_expectExceptions(Map.Entry<Integer, RawTag> tagEntry,
      Class<Throwable> expectedException) {
    FixMessageContext context = getContext(Map.ofEntries(tagEntry));

    assertThatThrownBy(() -> FixHeaderConverter.convert(context))
        .isInstanceOf(expectedException);
  }

  private static Stream<Arguments> invalidValues() {
    return Stream.of(
        Arguments.of(getRawTagEntry(8, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(9, "number"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(9, null), NumberFormatException.class),
        Arguments.of(getRawTagEntry(35, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(34, "number"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(34, null), NumberFormatException.class),
        Arguments.of(getRawTagEntry(52, "5/5/2015-12:00:01PM"), DateTimeParseException.class),
        Arguments.of(getRawTagEntry(52, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(49, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(56, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(90, "number"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(90, null), NumberFormatException.class),
        Arguments.of(getRawTagEntry(43, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(97, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(122, "5/5/2015-12:00:01PM"), DateTimeParseException.class),
        Arguments.of(getRawTagEntry(122, null), NullPointerException.class),
        Arguments.of(getRawTagEntry(212, "number"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(212, null), NumberFormatException.class),
        Arguments.of(getRawTagEntry(369, "number"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(369, null), NumberFormatException.class),
        Arguments.of(getRawTagEntry(627, "number"), NumberFormatException.class),
        Arguments.of(getRawTagEntry(627, null), NumberFormatException.class),
        Arguments.of(getRawTagEntry(629, "5/5/2015-12:00:01PM"), DateTimeParseException.class),
        Arguments.of(getRawTagEntry(629, null), NullPointerException.class)
    );
  }

}
