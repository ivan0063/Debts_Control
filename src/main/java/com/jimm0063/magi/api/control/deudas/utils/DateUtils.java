package com.jimm0063.magi.api.control.deudas.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DateUtils {
    public static List<String> getDateList(LocalDate startDate, LocalDate endDate) {
        // Formatter for the input
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("M/d/u");

        // Formatter for the output
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("MMM uuuu");

        // Parse strings to LocalDate instances
        startDate.format(inputFormatter);
        endDate.format(inputFormatter);

        return Stream.iterate(startDate.withDayOfMonth(1), date -> date.plusMonths(1))
                .limit(ChronoUnit.MONTHS.between(startDate, endDate.plusMonths(1)))
                .map(date -> date.format(outputFormatter))
                .collect(Collectors.toList());
    }
}
