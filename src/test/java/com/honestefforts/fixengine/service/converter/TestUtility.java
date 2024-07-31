package com.honestefforts.fixengine.service.converter;

import static com.honestefforts.fixengine.model.message.tags.TagType.STRING;

import com.honestefforts.fixengine.model.message.tags.RawTag;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class TestUtility {

  private static final DateTimeFormatter TIMESTAMP = DateTimeFormatter.ofPattern(
      "yyyyMMdd-HH:mm:ss");
  private static final DateTimeFormatter TIMESTAMP_MS = DateTimeFormatter.ofPattern(
      "yyyyMMdd-HH:mm:ss.SSS");
  private static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("HH:mm:ss");
  private static final DateTimeFormatter TIME_MS = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
  private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("yyyyMMdd");
  private static final DateTimeFormatter YEAR_MONTH = DateTimeFormatter.ofPattern("yyyyMM");

  public static String parseDateToString(LocalDate date) {
    return date.format(DATE);
  }

  public static String parseDateTimeToString(LocalDateTime dateTime) {
    return dateTime.format(TIMESTAMP);
  }

  public static String parseDateTimeMsToString(LocalDateTime dateTime) {
    return dateTime.format(TIMESTAMP_MS);
  }

  public static String parseYearMonthToString(YearMonth yearMonth) {
    return yearMonth.format(YEAR_MONTH);
  }

  /**
   * Get a simple RawTag where the only important things are tag and value
   */
  public static RawTag getRawTag(Integer tag, String value) {
    return RawTag.builder().tag(tag).value(value).dataType(STRING).build();
  }

  /**
   * Get a simple RawTag map entry where the only important things are tag and value
   */
  public static Map.Entry<Integer, RawTag> getRawTagEntry(Integer tag, String value) {
    return Map.entry(tag, getRawTag(tag, value));
  }

}
