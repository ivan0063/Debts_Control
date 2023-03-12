package com.jimm0063.magi.api.control.deudas.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "usuario_tarjeta")
@Setter @Getter
public class UserCard {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_usr_card", nullable = false, columnDefinition = "UUID default uuid_generate_v4()")
    private UUID idUserCard;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "dia_corte")
    private Integer cutOffDay;

    @Column(name = "activo")
    private Boolean active;

    @ManyToOne(targetEntity = Card.class)
    @JoinColumn(name = "id_tarjeta")
    private Card card;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "id_usuario")
    private User user;
}
