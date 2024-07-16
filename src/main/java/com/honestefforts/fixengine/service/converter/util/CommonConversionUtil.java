package com.honestefforts.fixengine.service.converter.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

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
    return Optional.of(utcDateTime.length())
        .filter(length -> length == 17)
        .map(_ -> LocalDateTime.parse(utcDateTime, TIMESTAMP))
        .orElse(LocalDateTime.parse(utcDateTime, TIMESTAMP_MS));
  }

  public static boolean parseBoolean(String boolStr) {
    return boolStr.equals(YES);
  }

  public static char parseChar(String character) {
    return character.charAt(0);
  }

  public static LocalDate parseDate(String date) {
    return LocalDate.parse(date, DATE);
  }

  public static YearMonth parseYearMonth(String yearMonth) {
    return yearMonth.length() == 6 ? YearMonth.parse(yearMonth, YEAR_MONTH)
        : YearMonth.parse(yearMonth.substring(0, 6), YEAR_MONTH);
  }
}
