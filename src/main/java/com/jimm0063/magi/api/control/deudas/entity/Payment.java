package com.jimm0063.magi.api.control.deudas.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "pago")
@Setter @Getter
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_pago")
    private Integer idPayment;

    @Column (name = "fecha_pago")
    private LocalDate paymentDate;

    @Column (name = "mes_pagado")
    private String monthPaymentMade;

    @Column (name = "activo")
    private Boolean active;

    @ManyToOne(targetEntity = UserCard.class)
    @JoinColumn(name = "id_usr_card")
    private UserCard userCard;
}
