package com.jimm0063.magi.api.control.deudas.models.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Setter @Getter
public class UpdateSalaryResponse {
    private Double newSalaryValue;
    private Double lastSalaryValue;
    private LocalDateTime updateDate;
}
