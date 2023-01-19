package com.jimm0063.magi.api.control.deudas.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "usuario_tarjeta")
@Setter @Getter
public class UserCard {
    @Id
    @Column(name = "id_usr_card")
    private Integer idUserCard;

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
