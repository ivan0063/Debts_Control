package com.jimm0063.magi.api.control.deudas.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "banco")
@Setter @Getter
public class Bank {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_banco", nullable = false, columnDefinition = "UUID default uuid_generate_v4()")
    private UUID idBank;

    @Column(name = "nombre_banco")
    private String bankName;
}
