package com.jimm0063.magi.api.control.deudas.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "actualizacion_salario")
@Setter @Getter
public class UpdateSalary {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_actualizacion_salario", nullable = false, columnDefinition = "UUID default uuid_generate_v4()")
    private UUID updateSalaryId;

    @Column(name = "nuevo_valor_salario")
    private Double newSalaryValue;

    @Column(name = "antiguo_valor_salario")
    private Double lastSalaryValue;

    @Column(name = "descripcion")
    private String description;

    @Column(name = "fecha de actualizacion")
    private Timestamp updateDate;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "id_usuario")
    private User user;
}
