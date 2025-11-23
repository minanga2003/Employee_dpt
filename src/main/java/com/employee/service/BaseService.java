package com.employee.service;

import org.springframework.data.domain.Sort;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.Locale;

public abstract class BaseService {

    protected BigDecimal nullSafe(BigDecimal value) {
        return value != null ? value : BigDecimal.ZERO;
    }

    protected int calculateAge(LocalDate dob) {
        return dob == null ? 0 : Period.between(dob, LocalDate.now()).getYears();
    }

    protected BigDecimal calculateTotalSalary(BigDecimal basic, BigDecimal travel, BigDecimal other) {
        return nullSafe(basic)
                .add(nullSafe(travel))
                .add(nullSafe(other));
    }

    protected String normalizeQuery(String query) {
        if (query == null || query.isBlank()) return null;
        return query.toLowerCase(Locale.ROOT).trim();
    }

    protected Sort buildSort(String sort) {
        if (sort == null || sort.isBlank()) {
            return Sort.by("empNo").ascending();
        }

        String trimmed = sort.trim();
        Sort.Direction direction = trimmed.startsWith("-")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        String property = trimmed.startsWith("-")
                ? trimmed.substring(1)
                : trimmed;

        if (property.isBlank()) property = "empNo";

        return Sort.by(direction, property);
    }
}
