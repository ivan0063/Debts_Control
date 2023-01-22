package com.jimm0063.magi.api.control.deudas.models.response;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter @Getter
public class UserResponse {
    private String name;
    private String lastName;
    private String email;
}
