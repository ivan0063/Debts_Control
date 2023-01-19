package com.jimm0063.magi.api.control.deudas.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "capital")
@Setter @Getter
public class Capital {
    @Id
    @Column(name = "id_capital")
    private Integer idCapital;

    @Column(name = "nombre_capital")
    private String capitalName;
}
