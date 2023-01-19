package com.jimm0063.magi.api.control.deudas.models.request;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class MultiDebtModel {
    private String debtName;
    private Integer debtDuration;
    private Integer monthlyInstallment;
    private Integer missingMonthlyPayments;
    private Double monthlyAmount;
}
