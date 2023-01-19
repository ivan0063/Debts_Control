package com.jimm0063.magi.api.control.deudas.models.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter @Getter
public class BankProjectionModelRequest {
    private String email;
    private String bank;
    private LocalDate projectionUntilDate;
}
