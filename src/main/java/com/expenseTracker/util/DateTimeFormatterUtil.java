package com.expenseTracker.util;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class DateTimeFormatterUtil {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public String formatLocalDateTime(LocalDateTime dateTime) {
        return dateTime.format(formatter);
    }
}