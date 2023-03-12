package com.jimm0063.magi.api.control.deudas.utils;

import com.jimm0063.magi.api.control.deudas.entity.Debt;
import com.jimm0063.magi.api.control.deudas.entity.DebtUpdate;
import com.jimm0063.magi.api.control.deudas.models.response.DebtModelResponse;
import com.jimm0063.magi.api.control.deudas.repository.DebtUpdateRepository;
import org.springframework.stereotype.Component;

@Component
public class ModelBuilder {
    private DebtUpdateRepository debtUpdateRepository;

    public ModelBuilder(DebtUpdateRepository debtUpdateRepository) {
        this.debtUpdateRepository = debtUpdateRepository;
    }

    public DebtModelResponse buildDebtModelResponse(Debt debt) {
        try {
            DebtUpdate debtUpdate = debtUpdateRepository.findFirstByDebtAndActiveIsTrueOrderByTimestampDesc(debt)
                    .orElseThrow(Exception::new);

            String installmentsOf = debtUpdate.getInstallment() + "/" + debt.getInstallments();
            String debtStarted = debt.getInitDate().getYear() + " " + debt.getInitDate().getMonth();
            String debtEnd = debt.getEndDate().getYear() + " " + debt.getEndDate().getMonth();

            return DebtModelResponse.builder()
                    .debtName(debt.getDebtName())
                    .totalAmount(debt.getTotalAmount())
                    .installmentsOf(installmentsOf)
                    .debtStarted(debtStarted)
                    .debtEnd(debtEnd)
                    .amountPaid(debtUpdate.getAmountPaid())
                    .monthlyPayment(debt.getMonthlyPayment())
                    .build();
        } catch (Exception e) {
            return null;
        }

    }
}
