package com.jimm0063.magi.api.control.deudas.models.response;

import com.jimm0063.magi.api.control.deudas.models.process.DebtMonthProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder(toBuilder = true)
@Setter @Getter
public class DebtProjectionResponse {
    private String debtName;
    private List<DebtMonthProjection> monthsProjection;
}
