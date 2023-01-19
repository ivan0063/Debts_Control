package com.jimm0063.magi.api.control.deudas.models.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Setter @Getter
public class DebtRowProjectionResponse {
    private String cardNickname;
    private List<DebtProjectionResponse> debtsProjection;
}
