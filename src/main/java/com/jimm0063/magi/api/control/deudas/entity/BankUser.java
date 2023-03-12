package com.jimm0063.magi.api.control.deudas.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "banco_usuario")
@Setter @Getter
public class BankUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_usr_bank", nullable = false, columnDefinition = "UUID default uuid_generate_v4()")
    private UUID idUserCard;

    @Column(name = "activo")
    private Boolean active;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "id_usuario")
    private User user;

    @ManyToOne(targetEntity = Bank.class)
    @JoinColumn(name = "id_banco")
    private Bank bank;
}
