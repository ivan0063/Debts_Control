package com.jimm0063.magi.api.control.deudas.models.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder(toBuilder = true)
@Setter @Getter
public class BankModelResponse {
    private String bankName;
    private List<CardDebtsModelResponse> cards;
}
