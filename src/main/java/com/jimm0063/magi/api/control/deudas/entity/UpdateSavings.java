package com.jimm0063.magi.api.control.deudas.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "actualizacion_ahorros")
@Setter @Getter
public class UpdateSavings {
    @Id
    @Column(name = "id_actualizacion_ahorros")
    private Integer updateSavvingsId;

    @Column(name = "nuevo_valor_ahorro")
    private String newSavingsValue;

    @Column(name = "antiguo_valor_ahorros")
    private String lastSavingValue;

    @Column(name = "fecha_actualizacion")
    private Timestamp updateDate;
}
