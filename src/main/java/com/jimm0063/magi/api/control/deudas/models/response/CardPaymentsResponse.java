package com.jimm0063.magi.api.control.deudas.models.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Builder
@Setter @Getter
public class CardPaymentsResponse {
    private String cardBank;
    private LocalDate lastPaymentMade;
    private List<LocalDate> payments;
}
