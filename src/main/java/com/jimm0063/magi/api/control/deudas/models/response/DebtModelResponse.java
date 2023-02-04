package com.jimm0063.magi.api.control.deudas.models.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Setter @Getter
public class DebtModelResponse {
    private String debtName;
    private Double totalAmount;
    private String installmentsOf;
    private String debtStarted;
    private String debtEnd;
    private Double amountPaid;
    private Double monthlyPayment;
    private String cardNickname;
}
