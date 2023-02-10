package com.jimm0063.magi.api.control.deudas.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "actualizacion_salario")
@Setter @Getter
public class UpdateSalary {
    @Id
    @Column(name = "id_actualizacion_salario")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer updateSalaryId;

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
