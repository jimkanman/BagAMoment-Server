package com.jkm.jimkanman.converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StringToDateTimeConverter {
    public static DateTimeFormatter isoFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public static LocalDateTime toLocalDateTime(String dateString) {
        try {
            return LocalDateTime.parse(dateString);
        } catch (Exception e) {
            System.out.println("StringToDateTimeConverter: WARNING - exception parsing " + dateString + ": " + e.getMessage());
            return null;
        }
    }

    public static LocalDateTime toLocalDateTime(String dateString, String format) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            return LocalDateTime.parse(dateString, formatter);
        } catch (Exception e) {
            System.out.println("StringToDateTimeConverter: WARNING - exception parsing " + dateString + " with format " + format + ": " + e.getMessage());
            return null;
        }
    }

    public static String toDateString(LocalDateTime dateTime) {
        try {
            return dateTime.format(isoFormatter);
        } catch (Exception e) {
            System.out.println("StringToDateTimeConverter: WARNING - exception parsing " + dateTime + ": exception = " + e.getMessage());
            return null;
        }
    }

    public static String toRequiredDateString(LocalDateTime dateTime) {
        return dateTime.format(isoFormatter);
    }

    public static String toDateString(LocalDateTime dateTime, String format) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            return dateTime.format(formatter);
        } catch (Exception e) {
            System.out.println("StringToDateTimeConverter: WARNING - exception parsing " + dateTime + " with format " + format + ": " + e.getMessage());
            return null;
        }
    }
}
