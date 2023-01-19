package com.jimm0063.magi.api.control.deudas.models.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Setter @Getter
public class ProjectionResponse {
    private String initDate;
    private String projectionUntil;
    private List<DebtRowProjectionResponse> debtRowsProjection;
}
