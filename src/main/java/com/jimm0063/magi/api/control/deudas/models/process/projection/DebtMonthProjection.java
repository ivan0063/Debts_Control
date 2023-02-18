package com.jimm0063.magi.api.control.deudas.models.process.projection;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Setter @Getter
public class DebtMonthProjection {
    private String month;
    private Double monthTotal;
    private List<DebtMonthStatus> debtsMonthStatus;
}
