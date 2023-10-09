package com.jimm0063.magi.api.control.deudas.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "usuario")
@Setter @Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_usuario", nullable = false, columnDefinition = "UUID default uuid_generate_v5()")
    private UUID userId;

    @Column(name = "nombre")
    private String name;

    @Column(name = "apellidos")
    private String lastName;

    @Column(name = "email")
    private String email;
}
