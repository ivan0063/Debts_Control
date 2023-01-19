package com.jimm0063.magi.api.control.deudas.models.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class DebtReqModel {
    private Integer actualInstallment;
    private String debtName;
    private LocalDate initDate;
    private Integer installments;
    private Double paidAmount;
    private Double totalAmount;
    private String cardNickname;
    private String user;
}
