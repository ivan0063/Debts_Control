package com.jimm0063.magi.api.control.deudas.models.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@SuperBuilder
@Setter @Getter
@NoArgsConstructor
public class ProjectionRequest {
    private String email;
    private LocalDate projectionUntil;
}
