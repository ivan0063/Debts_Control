package com.jimm0063.magi.api.control.deudas.models.process;

public record DebtMonthProjection(
        String month,
        Double paidAmount
) {
}
