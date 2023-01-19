package com.jimm0063.magi.api.control.deudas.models.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BankProjectionReq extends ProjectionRequest {
    private String bank;
}
