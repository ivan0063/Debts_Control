package com.jimm0063.magi.api.control.deudas.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "pago")
@Setter @Getter
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_pago")
    private Integer idPayment;

    @Column (name = "fecha_pago")
    private Timestamp paymentDate;

    @Column (name = "activo")
    private Boolean active;

    @ManyToOne(targetEntity = UserCard.class)
    @JoinColumn(name = "id_usr_card")
    private UserCard userCard;
}
