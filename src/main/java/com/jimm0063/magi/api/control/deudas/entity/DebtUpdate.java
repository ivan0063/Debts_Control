package com.jimm0063.magi.api.control.deudas.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "actualizacion_deuda")
@Setter @Getter
public class DebtUpdate {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_actualizacion_deuda", nullable = false, columnDefinition = "UUID default uuid_generate_v4()")
    private UUID idDebtUpdate;

    @Column(name = "mensualidad_anterior")
    private Integer lastInstallment;

    @Column(name = "nueva_mensualidad")
    private Integer installment;

    @Column(name = "monto_pagado_anterior")
    private Double lastAmountPaid;

    @Column(name = "monto_pagado_nuevo")
    private Double amountPaid;

    @Column(name = "descripcion")
    private String description;

    private Timestamp timestamp;

    @Column(name = "activo")
    private Boolean active;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "id_usuario")
    private User user;

    @ManyToOne(targetEntity = Debt.class)
    @JoinColumn(name = "id_deuda")
    private Debt debt;
}
