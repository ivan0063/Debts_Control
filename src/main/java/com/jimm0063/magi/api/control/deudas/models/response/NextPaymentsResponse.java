package com.jimm0063.magi.api.control.deudas.models.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Builder(toBuilder = true)
@Setter @Getter
public class NextPaymentsResponse {
    private String bankName;
    private Integer payday;
    private Double msiPayment;
}
