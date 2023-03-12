package com.jimm0063.magi.api.control.deudas.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "gasto_fijo")
@Setter @Getter
public class FixedExpense {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_gasto_fijo", nullable = false, columnDefinition = "UUID default uuid_generate_v4()")
    private UUID fixedExpendId;

    @Column(name = "nombre_gasto")
    private String expenseName;

    @Column(name = "compañia")
    private String company;

    @Column(name = "tipo")
    private String type;
}
