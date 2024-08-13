package com.honestefforts.fixengine.service.converter.util;

import io.micrometer.common.util.StringUtils;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import lombok.NonNull;

/** Intended for post-validation processing. All inputs are assumed to be compliant (not generate
 *   invalid input exceptions on the parser) as well as non-null. */
public class CommonConversionUtil {

  private static final DateTimeFormatter TIMESTAMP = DateTimeFormatter.ofPattern(
      "yyyyMMdd-HH:mm:ss");
  private static final DateTimeFormatter TIMESTAMP_MS = DateTimeFormatter.ofPattern(
      "yyyyMMdd-HH:mm:ss.SSS");
  private static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("HH:mm:ss");
  private static final DateTimeFormatter TIME_MS = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
  private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("yyyyMMdd");
  private static final DateTimeFormatter YEAR_MONTH = DateTimeFormatter.ofPattern("yyyyMM");
  private static final String YES = "Y";

  public static List<String> parseSpaceDelimitedList(String input) {
    return parseSpaceDelimitedList(input, in -> in);
  }

  public static <T> List<T> parseSpaceDelimitedList(String input, Function<String, T> parser) {
    return Arrays.stream(input.split("\\s+"))
        .filter(StringUtils::isNotBlank) //TODO: how much leeway should be allowed?
        .map(parser)
        .toList();
  }

  public static Character parseChar(String character) {
    return character.charAt(0);
  }

  public static boolean parseBoolean(String bool) {
    return bool.equals(YES);
  }

  public static LocalDate parseDate(String date) {
    return LocalDate.parse(date, DATE);
  }

  public static YearMonth parseYearMonth(String yearMonth) {
    return yearMonth.length() == 6 ?
        YearMonth.parse(yearMonth, YEAR_MONTH)
        : YearMonth.parse(yearMonth.substring(0, 6), YEAR_MONTH);
  }

  public static LocalDateTime parseUtcTimestamp(String utcDateTime) {
    return utcDateTime.length() == 17 ?
        LocalDateTime.parse(utcDateTime, TIMESTAMP)
        : LocalDateTime.parse(utcDateTime, TIMESTAMP_MS);
  }

  public static LocalTime parseTime(String time) {
    return time.length() == 8 ?
        LocalTime.parse(time, TIME)
        : LocalTime.parse(time, TIME_MS);
  }

  //the below wrappers are here to centralize these conversions in case additional requirements arise
  public static int parseInt(String integer) {
    return Integer.parseInt(integer);
  }

  public static double parseDouble(String decimal) {
    return Double.parseDouble(decimal);
  }

  public static <T extends Enum<T>> T parseEnum(@NonNull Class<T> enumClass, String enumString) {
    return Enum.valueOf(enumClass, enumString);
  }

}
