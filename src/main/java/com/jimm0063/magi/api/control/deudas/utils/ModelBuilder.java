package com.jimm0063.magi.api.control.deudas.utils;

import com.jimm0063.magi.api.control.deudas.entity.Debt;
import com.jimm0063.magi.api.control.deudas.models.response.DebtModelResponse;

public class ModelBuilder {
    public static DebtModelResponse buildDebtModelResponse(Debt debt) {
        String installmentsOf = debt.getCurrentInstallment() + "/" + debt.getInstallments();
        String debtStarted = debt.getInitDate().getYear() + " " + debt.getInitDate().getMonth();
        String debtEnd = debt.getEndDate().getYear() + " " + debt.getEndDate().getMonth();

        return DebtModelResponse.builder()
                .debtName(debt.getDebtName())
                .totalAmount(debt.getTotalAmount())
                .installmentsOf(installmentsOf)
                .debtStarted(debtStarted)
                .debtEnd(debtEnd)
                .amountPaid(debt.getAmountPaid())
                .monthlyPayment(debt.getMonthlyPayment())
                .build();
    }
}
