package com.jimm0063.magi.api.control.deudas.models.response.projection;

import com.jimm0063.magi.api.control.deudas.models.process.projection.DebtMonthProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Setter @Getter
public class CardProjection {
    private String cardNickname;
    private List<DebtMonthProjection> debtMonthProjections;
}
