package com.urise.webapp.util;

import java.time.LocalDate;
import java.time.Month;

public class DateUtil {

    public static LocalDate of(int year, Month month) {
        return LocalDate.of(year, month, 1);
    }

    public static LocalDate parse(String date) {
        if (date.isEmpty()) {
            return LocalDate.now();
        }
        return LocalDate.parse(date);
    }
}
