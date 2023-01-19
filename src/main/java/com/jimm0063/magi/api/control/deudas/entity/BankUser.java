package com.jimm0063.magi.api.control.deudas.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "banco_usuario")
@Setter @Getter
public class BankUser {
    @Id
    @Column(name = "id_usr_bank")
    private Integer idUserCard;

    @Column(name = "activo")
    private Boolean active;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "id_usuario")
    private User user;

    @ManyToOne(targetEntity = Bank.class)
    @JoinColumn(name = "id_banco")
    private Bank bank;
}
