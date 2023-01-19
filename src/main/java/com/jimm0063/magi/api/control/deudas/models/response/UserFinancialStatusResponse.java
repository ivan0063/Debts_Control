package com.jimm0063.magi.api.control.deudas.models.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Builder
@Setter @Getter
public class UserFinancialStatusResponse {
    private String email;
    private Double totalMonthlyDebtPayment;
    private Double totalDebt;
    private Integer debtCount;
    private Object incomes;
    private List<Map> debtSpecification;
}
