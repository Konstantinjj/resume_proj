package com.urise.webapp.util;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    public static final LocalDate NOW = LocalDate.of(3000, 1, 1);

    public static LocalDate of(int year, Month month) {
        return LocalDate.of(year, month, 1);
    }

    public static String format(LocalDate date) {
        if (date == null) return "";
        return date.equals(NOW) ? "Сейчас" : date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public static LocalDate parse(String date) {
        LocalDate parsedDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        if (date.isEmpty() || parsedDate.isAfter(LocalDate.now())) {
            return of(3000, Month.of(1));
        }
        return parsedDate;
    }
}
