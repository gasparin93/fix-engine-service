package com.honestefforts.fixengine.service.converter.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import lombok.NonNull;

/** Intended for post-validation processing, all inputs are assumed non-null */
public class CommonConversionUtil {

  private static final DateTimeFormatter TIMESTAMP = DateTimeFormatter.ofPattern(
      "yyyyMMdd-HH:mm:ss");
  private static final DateTimeFormatter TIMESTAMP_MS = DateTimeFormatter.ofPattern(
      "yyyyMMdd-HH:mm:ss.SSS");
  private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("yyyyMMdd");
  private static final DateTimeFormatter YEAR_MONTH = DateTimeFormatter.ofPattern("yyyyMM");
  private static final String YES = "Y";

  public static LocalDateTime parseUtcTimestamp(String utcDateTime) {
    if(utcDateTime == null) {
      return null;
    }
    return Optional.of(utcDateTime.length())
        .filter(length -> length == 17)
        .map(_ -> LocalDateTime.parse(utcDateTime, TIMESTAMP))
        .orElseGet(() -> LocalDateTime.parse(utcDateTime, TIMESTAMP_MS));
  }

  public static Double parseDouble(String decimal) {
    return Optional.ofNullable(decimal)
        .map(Double::parseDouble)
        .orElse(null);
  }

  public static Integer parseInt(String integer) {
    return Optional.ofNullable(integer)
        .map(Integer::parseInt)
        .orElse(null);
  }

  public static Boolean parseBoolean(String boolStr) {
    return Optional.ofNullable(boolStr)
        .map(str -> str.equals(YES))
        .orElse(null);
  }

  public static Character parseChar(String character) {
    return Optional.ofNullable(character)
        .map(ch -> ch.charAt(0))
        .orElse(null);
  }

  public static List<String> parseSpaceDelimitedList(String input) {
    return parseSpaceDelimitedList(input, in -> in);
  }

  public static <T> List<T> parseSpaceDelimitedList(String input, Function<String, T> parser) {
    if (input == null || input.isBlank()) {
      return List.of();
    }

    return Arrays.stream(input.split("\\s+"))
        .map(parser)
        .filter(Objects::nonNull)
        .toList();
  }

  public static LocalDate parseDate(String date) {
    return Optional.ofNullable(date)
        .map(dt -> LocalDate.parse(dt, DATE))
        .orElse(null);
  }

  public static <T extends Enum<T>> T parseEnum(@NonNull Class<T> enumClass, String enumString) {
    return Optional.ofNullable(enumString)
        .map(str -> {
          try {
            return Enum.valueOf(enumClass, str);
          } catch (Exception e) {
            return null;
          }
        })
        .orElse(null);
  }

  public static YearMonth parseYearMonth(String yearMonth) {
    if(yearMonth == null) {
      return null;
    }
    return Optional.of(yearMonth.length())
        .filter(length -> length == 6)
        .map(_ -> YearMonth.parse(yearMonth, YEAR_MONTH))
        .orElseGet(() -> YearMonth.parse(yearMonth.substring(0, 6), YEAR_MONTH));
  }

}
