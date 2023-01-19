package com.jimm0063.magi.api.control.deudas.models.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder(toBuilder = true)
@Getter @Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse {
    private Integer errorCode;
    private String responseMessage;
    private Object responseObject;
}
