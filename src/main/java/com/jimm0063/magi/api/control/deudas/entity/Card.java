package com.jimm0063.magi.api.control.deudas.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tarjeta")
@Setter @Getter
public class Card {
    @Id
    @Column(name = "id_tarjeta")
    private Integer cardId;

    @Column(name = "nombre_tarjeta")
    private String cardName;

    @ManyToOne(targetEntity = Bank.class)
    @JoinColumn(name = "id_banco")
    private Bank bank;
}
