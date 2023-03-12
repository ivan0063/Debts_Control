package com.jimm0063.magi.api.control.deudas.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "tarjeta")
@Setter @Getter
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_tarjeta", nullable = false, columnDefinition = "UUID default uuid_generate_v4()")
    private UUID cardId;

    @Column(name = "nombre_tarjeta")
    private String cardName;

    @ManyToOne(targetEntity = Bank.class)
    @JoinColumn(name = "id_banco")
    private Bank bank;
}
