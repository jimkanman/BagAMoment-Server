package com.jkm.jimkanman.converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StringToDateTimeConverter {
    public static DateTimeFormatter isoFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public static LocalDateTime toLocalDateTime(String dateString) {
        return LocalDateTime.parse(dateString);
    }

    public static LocalDateTime toLocalDateTime(String dateString, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return LocalDateTime.parse(dateString, formatter);
    }

    public static String toDateString(LocalDateTime dateTime) {
        return dateTime.format(isoFormatter);
    }

    public static String toDateString(LocalDateTime dateTime, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return dateTime.format(formatter);
    }
}
