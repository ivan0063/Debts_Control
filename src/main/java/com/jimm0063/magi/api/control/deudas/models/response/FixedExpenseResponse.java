package com.jimm0063.magi.api.control.deudas.models.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter @Getter
public class FixedExpenseResponse {
    private String expenseName;
    private String company;
    private Double amount;
    private Integer paymentDate;
}
