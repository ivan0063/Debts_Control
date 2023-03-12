package com.jimm0063.magi.api.control.deudas.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "capital")
@Setter @Getter
public class Capital {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_capital", nullable = false, columnDefinition = "UUID default uuid_generate_v4()")
    private UUID idCapital;

    @Column(name = "nombre_capital")
    private String capitalName;
}
