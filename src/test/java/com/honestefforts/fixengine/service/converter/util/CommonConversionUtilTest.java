package com.honestefforts.fixengine.service.converter.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.honestefforts.fixengine.model.universal.Currency;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

/** Intended for post-validation processing, all inputs are assumed non-null */
public class CommonConversionUtilTest {

  @Test
  void parseSpaceDelimitedList_strings_happyPath() {
    assertThat(CommonConversionUtil.parseSpaceDelimitedList("str1 str2"))
        .containsExactly("str1", "str2");
  }

  @Test
  void parseSpaceDelimitedList_integers_happyPath() {
    assertThat(CommonConversionUtil.parseSpaceDelimitedList("1 2", Integer::parseInt))
        .containsExactly(1, 2);
  }

  @Test
  void parseSpaceDelimitedList_includesBlankValues_expectEmptyList() {
    assertThat(CommonConversionUtil.parseSpaceDelimitedList("    "))
        .isEmpty();
  }

  @Test
  void parseSpaceDelimitedList_null_expectNullPointerException() {
    assertThatThrownBy(() -> CommonConversionUtil.parseSpaceDelimitedList(null))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  void parseSpaceDelimitedList_invalidParser_expectNumberFormatException() {
    assertThatThrownBy(() -> CommonConversionUtil.parseSpaceDelimitedList("str str", Integer::parseInt))
        .isInstanceOf(NumberFormatException.class);
  }

  @Test
  void parseChar_oneChar_happyPath() {
    assertThat(CommonConversionUtil.parseChar("s"))
        .isEqualTo('s');
  }

  @Test
  void parseChar_multipleChars_firstIsExtracted() {
    assertThat(CommonConversionUtil.parseChar("sea monkey"))
        .isEqualTo('s');
  }

  @Test
  void parseChar_emptyString_expectStringOutOfBoundsException() {
    assertThatThrownBy(() -> CommonConversionUtil.parseChar(""))
        .isInstanceOf(StringIndexOutOfBoundsException.class);
  }

  @Test
  void parseChar_null_expectNullPointerException() {
    assertThatThrownBy(() -> CommonConversionUtil.parseChar(null))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  void parseBoolean_Y_expectTrue() {
    assertThat(CommonConversionUtil.parseBoolean("Y"))
        .isEqualTo(true);
  }

  @ParameterizedTest
  @ValueSource(strings = {"N", "no", "yes", ""})
  void parseBoolean_nonY_expectFalse(String input) {
    //TODO: how strict should we be? IllegalArgumentException if not N or Y?
    assertThat(CommonConversionUtil.parseBoolean(input))
        .isEqualTo(false);
  }

  @Test
  void parseBoolean_null_expectNullPointerException() {
    assertThatThrownBy(() -> CommonConversionUtil.parseBoolean(null))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  void parseDate_happyPath() {
    assertThat(CommonConversionUtil.parseDate("20240731"))
        .isEqualTo(LocalDate.of(2024, 7, 31));
  }

  @Test
  void parseDate_badlyFormatted_expectException() {
    assertThatThrownBy(() -> CommonConversionUtil.parseDate(("2024--07/31")))
        .isInstanceOf(DateTimeParseException.class);
  }

  @Test
  void parseDate_null_expectNullPointerException() {
    assertThatThrownBy(() -> CommonConversionUtil.parseDate(null))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  void parseYearMonth_happyPath() {
    assertThat(CommonConversionUtil.parseYearMonth("202407"))
        .isEqualTo(YearMonth.of(2024, 7));
  }

  @Test
  void parseYearMonth_longerThanYYYYMM_convertTheFirst6CharsSuccessfully() {
    assertThat(CommonConversionUtil.parseYearMonth("20240731"))
        .isEqualTo(YearMonth.of(2024, 7));
  }

  @Test
  void parseYearMonth_badlyFormatted_expectException() {
    assertThatThrownBy(() -> CommonConversionUtil.parseYearMonth(("2024/07")))
        .isInstanceOf(DateTimeParseException.class);
  }

  @Test
  void parseYearMonth_null_expectNullPointerException() {
    assertThatThrownBy(() -> CommonConversionUtil.parseYearMonth(null))
        .isInstanceOf(NullPointerException.class);
  }

  @ParameterizedTest
  @CsvSource({"20240731-23:45:01", "20240731-23:45:01.123"})
  void parseUtcTimestamp_happyPath(String input) {
    assertThat(CommonConversionUtil.parseUtcTimestamp(input))
        .isEqualTo(LocalDateTime.of(2024, 7, 31, 23, 45, 1,  123999999)
            .truncatedTo(input.length() == 17 ? ChronoUnit.SECONDS : ChronoUnit.MILLIS));
  }

  @Test
  void parseUtcTimestamp_badlyFormatted_expectException() {
    assertThatThrownBy(() -> CommonConversionUtil.parseUtcTimestamp(("2024/07")))
        .isInstanceOf(DateTimeParseException.class);
  }

  @Test
  void parseUtcTimestamp_null_expectNullPointerException() {
    assertThatThrownBy(() -> CommonConversionUtil.parseUtcTimestamp(null))
        .isInstanceOf(NullPointerException.class);
  }

  @ParameterizedTest
  @CsvSource({"23:45:01", "23:45:01.123"})
  void parseTime_happyPath(String input) {
    assertThat(CommonConversionUtil.parseTime(input))
        .isEqualTo(LocalTime.of(23, 45, 1,  123999999)
            .truncatedTo(input.length() == 8 ? ChronoUnit.SECONDS : ChronoUnit.MILLIS));
  }

  @Test
  void parseTime_badlyFormatted_expectException() {
    assertThatThrownBy(() -> CommonConversionUtil.parseTime(("2024/07")))
        .isInstanceOf(DateTimeParseException.class);
  }

  @Test
  void parseTime_null_expectNullPointerException() {
    assertThatThrownBy(() -> CommonConversionUtil.parseTime(null))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  void parseInt_happyPath() {
    assertThat(CommonConversionUtil.parseInt("1"))
        .isEqualTo(1);
  }

  @Test
  void parseInt_invalidEntries_expectNumberFormatException() {
    assertThatThrownBy(() -> CommonConversionUtil.parseInt(null))
        .isInstanceOf(NumberFormatException.class);
    assertThatThrownBy(() -> CommonConversionUtil.parseInt("five"))
        .isInstanceOf(NumberFormatException.class);
  }

  @Test
  void parseDouble_happyPath() {
    assertThat(CommonConversionUtil.parseDouble("1.5"))
        .isEqualTo(1.5);
  }

  @Test
  void parseDouble_invalidInput_expectNumberFormatException() {
    assertThatThrownBy(() -> CommonConversionUtil.parseDouble("123,456.0X"))
        .isInstanceOf(NumberFormatException.class);
  }

  @Test
  void parseDouble_null_expectNullPointerException() {
    assertThatThrownBy(() -> CommonConversionUtil.parseDouble(null))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  void parseEnum_happyPath() {
    assertThat(CommonConversionUtil.parseEnum(Currency.class, Currency.USD.name()))
        .isEqualTo(Currency.USD);
  }

  @Test
  void parseEnum_invalidInput_expectIllegalArgumentException() {
    assertThatThrownBy(() -> CommonConversionUtil.parseEnum(Currency.class, "_ABC_"))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void parseEnum_nullEntries_expectNullPointerException() {
    assertThatThrownBy(() -> CommonConversionUtil.parseEnum(null, "USD"))
        .isInstanceOf(NullPointerException.class);

    assertThatThrownBy(() -> CommonConversionUtil.parseEnum(Currency.class, null))
        .isInstanceOf(NullPointerException.class);
  }

}
