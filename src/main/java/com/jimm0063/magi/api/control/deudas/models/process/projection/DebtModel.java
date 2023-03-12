package com.jimm0063.magi.api.control.deudas.models.process.projection;

import com.jimm0063.magi.api.control.deudas.entity.UserCard;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter @Getter
@AllArgsConstructor
@NoArgsConstructor
public class DebtModel {
    private Double totalAmount;
    private Integer installments;
    private LocalDate initDate;
    private LocalDate endDate;
    private String debtName;
    private Double monthlyPayment;
    private Boolean active;
    private UserCard userCard;
    private Integer currentInstallment;
    private Double amountPaid;
}

