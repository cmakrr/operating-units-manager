package com.rnpc.operatingunit.util;

import org.apache.commons.lang.time.DurationFormatUtils;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateFormattingUtil {
    private static final String HOUR_MINUTE_PATTERN = "H:mm";
    private static final String HOUR_MINUTE_SECOND_PATTERN = HOUR_MINUTE_PATTERN + ":ss";
    private static final String DAY_MONTH_YEAR_PATTERN = "dd.MM.yyyy";
    private static final String RUSSIAN_HOUR = " ч ";
    private static final String RUSSIAN_MINUTE = " мин ";
    private static final String RUSSIAN_SECOND = " с ";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DAY_MONTH_YEAR_PATTERN);
    private static final DateTimeFormatter HOUR_MINUTE_FORMATTER = DateTimeFormatter.ofPattern(HOUR_MINUTE_PATTERN);
    private static final DateTimeFormatter HOUR_MINUTE_SECOND_FORMATTER =
            DateTimeFormatter.ofPattern(HOUR_MINUTE_SECOND_PATTERN);

    public static String formatToHoursMinutesAndSeconds(LocalDateTime localDateTime) {
        return localDateTime.format(HOUR_MINUTE_SECOND_FORMATTER);
    }

    public static String formatToHoursAndMinutes(LocalDateTime localDateTime) {
        return localDateTime.format(HOUR_MINUTE_FORMATTER);
    }

    public static String formatDate(LocalDate date) {
        return date.format(DATE_FORMATTER);
    }

    public static String formatDuration(Duration duration) {
        return DurationFormatUtils.formatDuration(duration.toMillis(), HOUR_MINUTE_SECOND_PATTERN, true);
    }

    public static String formatRussianDuration(Duration duration) {
        int hours = duration.toHoursPart();
        int minutes = duration.toMinutesPart();
        int seconds = duration.toSecondsPart();

        StringBuilder result = new StringBuilder();
        if (hours > 0) {
            result.append(hours).append(RUSSIAN_HOUR);
        }
        if (minutes > 0) {
            result.append(minutes).append(RUSSIAN_MINUTE);
        }
        if (seconds > 0 || result.length() == 0) {
            result.append(seconds).append(RUSSIAN_SECOND);
        }

        return result.toString().trim();
    }
}
