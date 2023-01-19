package com.jimm0063.magi.api.control.deudas.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "capital_usuario")
@Setter @Getter
public class CapitalUser {
    @Id
    @Column(name = "id_usr_capital")
    private Double idUsrCapital;

    @Column(name = "monto")
    private Double amount;

    @Column(name = "activo")
    private Boolean activo;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "id_usuario")
    private User user;

    @ManyToOne(targetEntity = Capital.class)
    @JoinColumn(name = "id_capital")
    private Capital capital;
}
