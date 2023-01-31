package com.jimm0063.magi.api.control.deudas.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "actualizacion_ahorros")
@Setter @Getter
public class UpdateSavings {
    @Id
    @Column(name = "id_actualizacion_ahorros")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer updateSavvingsId;

    @Column(name = "nuevo_valor_ahorro")
    private Double newSavingsValue;

    @Column(name = "antiguo_valor_ahorros")
    private Double lastSavingValue;

    @Column(name = "fecha_actualizacion")
    private Timestamp updateDate;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "id_usuario")
    private User user;
}
