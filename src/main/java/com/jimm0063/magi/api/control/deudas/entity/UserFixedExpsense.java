package com.jimm0063.magi.api.control.deudas.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "usuario_gasto_fijo")
@Setter @Getter
public class UserFixedExpsense {
    @Id
    @Column(name = "id_usr_expense")
    private Integer idUsrExpense;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "id_usuario")
    private User user;

    @ManyToOne(targetEntity = FixedExpense.class)
    @JoinColumn(name = "id_gasto_fijo")
    private FixedExpense fixedExpense ;

    @Column(name = "monto")
    private Double amount;

    @Column(name = "dia_pago")
    private Integer paymentDay;

    @Column(name = "activo")
    private Boolean active;
}
