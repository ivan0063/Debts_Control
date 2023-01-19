package com.jimm0063.magi.api.control.deudas.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "banco")
@Setter @Getter
public class Bank {
    @Id
    @Column(name = "id_banco")
    private Integer idBank;

    @Column(name = "nombre_banco")
    private String bankName;
}
