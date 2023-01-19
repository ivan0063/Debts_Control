package com.jimm0063.magi.api.control.deudas.models.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder(toBuilder = true)
@Setter @Getter
public class StreamingResponseModel {
    private String email;
    private Double totalAmount;
    private Object streamingServices;
}
