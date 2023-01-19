package com.jimm0063.magi.api.control.deudas.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "usuario")
@Setter @Getter
public class User {
    @Id
    @Column(name = "id_usuario")
    private Integer userId;

    @Column(name = "nombre")
    private String name;

    @Column(name = "apellidos")
    private String lastName;

    @Column(name = "email")
    private String email;
}
