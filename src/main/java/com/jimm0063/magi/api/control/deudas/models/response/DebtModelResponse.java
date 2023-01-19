package com.jimm0063.magi.api.control.deudas.models.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder(toBuilder = true)
@Setter @Getter
public class DebtModelResponse {
    private String debtName;
    private Double totalAmount;
    private String installmentsOf;
    private String debtStarted;
    private String debtEnd;
    private Double amountPaid;
    private Double monthlyPayment;
}
