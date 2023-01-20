package com.jimm0063.magi.api.control.deudas.models.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Setter @Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectionResponse {
    private String initDate;
    private String projectionUntil;
    private List<DebtRowProjectionResponse> debtRowsProjection;
    private DebtRowProjectionResponse debtRowProjection;
}
