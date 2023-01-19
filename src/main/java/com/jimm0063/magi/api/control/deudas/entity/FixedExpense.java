package com.jimm0063.magi.api.control.deudas.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "gasto_fijo")
@Setter @Getter
public class FixedExpense {
    @Id
    @Column(name = "id_gasto_fijo")
    private Integer fixedExpendId;

    @Column(name = "nombre_gasto")
    private String expenseName;

    @Column(name = "compañia")
    private String company;

    @Column(name = "tipo")
    private String type;
}
