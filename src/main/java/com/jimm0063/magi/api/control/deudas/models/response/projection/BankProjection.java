package com.jimm0063.magi.api.control.deudas.models.response.projection;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Setter @Getter
public class BankProjection {
    private String bankName;
    private List<CardProjection> cardProjections;
}
