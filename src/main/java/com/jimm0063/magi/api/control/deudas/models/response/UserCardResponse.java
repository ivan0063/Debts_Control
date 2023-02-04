package com.jimm0063.magi.api.control.deudas.models.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter @Getter
public class UserCardResponse {
    private String nickname;
    private Integer cutOffDay;
    private String cardName;
    private String bankName;
}
