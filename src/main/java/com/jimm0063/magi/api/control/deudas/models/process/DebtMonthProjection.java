package com.jimm0063.magi.api.control.deudas.models.process;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter @Getter
public class DebtMonthProjection {
    private String mont;
    private Double paidAmount;
    private Double totalDebtAmount;
    private Double monthlyAmount;
}
