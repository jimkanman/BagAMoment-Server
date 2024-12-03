package com.jkm.jimkanman.converter;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import static org.assertj.core.api.Assertions.*;

class StringToDateTimeConverterTest {

    @Test
    void testToLocalDateTime() {
        // Given
        String dateString = "2024-11-24T15:30:00";

        // When
        LocalDateTime result = StringToDateTimeConverter.toLocalDateTime(dateString);

        // Then
        assertThat(result).isEqualTo(LocalDateTime.of(2024, 11, 24, 15, 30, 0));
    }

    @Test
    void testToLocalDateTime_CustomFormat() {
        // Given
        String dateString = "24-11-2024 15:30:00";
        String format = "dd-MM-yyyy HH:mm:ss";

        // When
        LocalDateTime result = StringToDateTimeConverter.toLocalDateTime(dateString, format);

        // Then
        assertThat(result).isEqualTo(LocalDateTime.of(2024, 11, 24, 15, 30, 0));
    }

    @Test
    void testToDateString() {
        // Given
        LocalDateTime dateTime = LocalDateTime.of(2024, 11, 24, 15, 30, 0);

        // When
        String result = StringToDateTimeConverter.toDateString(dateTime);

        // Then
        assertThat(result).isEqualTo("2024-11-24T15:30:00");
    }

    @Test
    void testToDateString_CustomFormat() {
        // Given
        LocalDateTime dateTime = LocalDateTime.of(2024, 11, 24, 15, 30, 0);
        String format = "dd-MM-yyyy HH:mm:ss";

        // When
        String result = StringToDateTimeConverter.toDateString(dateTime, format);

        // Then
        assertThat(result).isEqualTo("24-11-2024 15:30:00");
    }

    @Test
    void testInvalidFormat_inToLocalDateTime() {
        // Given
        String dateString = "24/11/2024 15:30:00";
        String invalidFormat = "dd-MM-yyyy HH:mm:ss";

        // When/Then
        assertThatThrownBy(() -> StringToDateTimeConverter.toLocalDateTime(dateString, invalidFormat))
                .isInstanceOf(DateTimeParseException.class);
    }

    @Test
    void testInvalidFormat_inToDateString() {
        // Given
        LocalDateTime dateTime = LocalDateTime.of(2024, 11, 24, 15, 30, 0);
        String invalidFormat = "invalid_format";

        // When/Then
        assertThatThrownBy(() -> StringToDateTimeConverter.toDateString(dateTime, invalidFormat))
                .isInstanceOf(IllegalArgumentException.class);
    }
}