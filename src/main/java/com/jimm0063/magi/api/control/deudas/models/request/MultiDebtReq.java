package com.jimm0063.magi.api.control.deudas.models.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class MultiDebtReq {
    private String cardNickname;
    private String email;
    private List<MultiDebtModel> debtData;
}
