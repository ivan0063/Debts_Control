package com.jimm0063.magi.api.control.deudas.models.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Setter @Getter
public class UserPaymentResponse {
    private String nickname;
    private Integer cutOffDay;
    private List<LocalDateTime> paymentsMade;
}
