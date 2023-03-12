package com.jimm0063.magi.api.control.deudas.models.response.projection;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Builder
@Setter @Getter
public class ProjectionResponse {
    private Double monthlyDebtPayment;
    private Double extraMonthSaving;
    private Double savingsTotal;
    //private List<DebtMonthStatus> debts;
    private Map<String, Double > debts;
}
