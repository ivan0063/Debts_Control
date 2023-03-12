package com.jimm0063.magi.api.control.deudas.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "deuda")
@Setter @Getter
@NoArgsConstructor
public class Debt {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_deuda", nullable = false, columnDefinition = "UUID default uuid_generate_v4()")
    private UUID debtId;

    @Column(name = "monto_total")
    private Double totalAmount;

    @Column(name = "parcialidades")
    private Integer installments;

    @Column(name = "fecha_inicio")
    private LocalDate initDate;

    @Column(name = "fecha_fin")
    private LocalDate endDate;

    @Column(name = "nombre_deuda")
    private String debtName;

    @Column(name = "monto_mensualidad")
    private Double monthlyPayment;

    @Column(name = "activa")
    private Boolean active;

    @ManyToOne(targetEntity = UserCard.class)
    @JoinColumn(name = "id_usr_card")
    private UserCard userCard;
}
