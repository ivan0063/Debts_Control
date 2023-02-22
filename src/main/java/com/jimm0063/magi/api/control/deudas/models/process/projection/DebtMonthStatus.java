package com.jimm0063.magi.api.control.deudas.models.process.projection;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter @Getter
public class DebtMonthStatus {
    private String debtName;
    private String currentMonth;
    private String bankName;
    private String cardName;
    private Double currentMonthPayment;
}
