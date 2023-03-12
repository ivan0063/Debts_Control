package com.jimm0063.magi.api.control.deudas.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "capital_usuario")
@Setter @Getter
public class CapitalUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_usr_capital", nullable = false, columnDefinition = "UUID default uuid_generate_v4()")
    private UUID idUsrCapital;

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
