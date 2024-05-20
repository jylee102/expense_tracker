package com.expenseTracker.util;

import org.springframework.stereotype.Component;

import java.text.DecimalFormat;

@Component
public class NumberFormatterUtil {

    public static String formatAmount(double amount) {
        return String.format("%,.0f원", amount);
    }

    public static String roundOutPercentageDecimals(double decimal) {
        DecimalFormat df = new DecimalFormat("#");
        return df.format(Math.round(decimal)) + "%";
    }
}