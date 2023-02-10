package com.jimm0063.magi.api.control.deudas.models.request;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class SalaryUpdateRequestModel {
    private String email;
    private Double salaryAmountUpdate;
    private String description;
}
