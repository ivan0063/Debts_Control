package com.jimm0063.magi.api.control.deudas.service;

import com.jimm0063.magi.api.control.deudas.entity.Debt;
import com.jimm0063.magi.api.control.deudas.entity.Payment;
import com.jimm0063.magi.api.control.deudas.entity.UserCard;
import com.jimm0063.magi.api.control.deudas.models.process.DebtMonthProjection;
import com.jimm0063.magi.api.control.deudas.models.response.DebtProjectionResponse;
import com.jimm0063.magi.api.control.deudas.models.response.DebtRowProjectionResponse;
import com.jimm0063.magi.api.control.deudas.models.response.ProjectionResponse;
import com.jimm0063.magi.api.control.deudas.repository.DebtRepository;
import com.jimm0063.magi.api.control.deudas.repository.PaymentRepository;
import com.jimm0063.magi.api.control.deudas.repository.UserCardRepository;
import com.jimm0063.magi.api.control.deudas.utils.DateUtils;
import jakarta.persistence.EntityExistsException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ProjectionService {
    private final UserCardRepository userCardRepository;
    private final DebtRepository debtRepository;
    private final PaymentRepository paymentRepository;

    public ProjectionService(UserCardRepository userCardRepository, DebtRepository debtRepository, PaymentRepository paymentRepository) {
        this.userCardRepository = userCardRepository;
        this.debtRepository = debtRepository;
        this.paymentRepository = paymentRepository;
    }

    public DebtRowProjectionResponse buildDebtRow(UserCard userCard, LocalDate projectionUntilDate) {
        // Get Months
        List<String> months = DateUtils.getDateList(LocalDate.now(), projectionUntilDate);
        List<Debt> debts = debtRepository.findAllByUserCardAndActive(userCard, true);

        List<DebtProjectionResponse> debtProjectionResponses = new ArrayList<>();
        for (Debt debt : debts) {
            List<DebtMonthProjection> debtMonthProjections = new ArrayList<>();
            double amountPaid = debt.getAmountPaid();
            // Look for the payments made on that month
            LocalDate preset = LocalDate.now();
            List<Payment> paymentsMadeThisMonth = paymentRepository
                    .paymentsMadeThisMonth(preset.getMonth().getValue(), preset.getYear(), userCard);
            if(paymentsMadeThisMonth.isEmpty()) amountPaid += debt.getMonthlyPayment();

            // Calculating projection over all the months of the projection
            for (String month : months) {
                if (amountPaid <= debt.getTotalAmount()) {
                    debtMonthProjections.add(DebtMonthProjection.builder()
                            .mont(month)
                            .paidAmount(amountPaid)
                            .monthlyAmount(debt.getMonthlyPayment())
                            .totalDebtAmount(debt.getTotalAmount())
                            .build());

                    amountPaid += debt.getMonthlyPayment();
                }
            }

            debtProjectionResponses.add(DebtProjectionResponse.builder()
                    .debtName(debt.getDebtName())
                    .monthsProjection(debtMonthProjections)
                    .build());
        }

        return DebtRowProjectionResponse.builder()
                .cardNickname(userCard.getNickname())
                .debtsProjection(debtProjectionResponses)
                .build();
    }

    public ProjectionResponse bankProjection(String email, String bankName, LocalDate projectionUntilDate) {
        List<UserCard> userCards = userCardRepository.findAllByCard_Bank_BankNameAndUser_EmailAndActiveIsTrue(bankName, email);
        // Building debts row
        List<DebtRowProjectionResponse> debtsRow = userCards.stream().map(userCard -> this.buildDebtRow(userCard, projectionUntilDate))
                .collect(Collectors.toList());

        return ProjectionResponse.builder()
                .initDate(DateUtils.outputFormatDate(LocalDate.now()))
                .projectionUntil(DateUtils.outputFormatDate(projectionUntilDate))
                .debtRowsProjection(debtsRow)
                .build();
    }

    public ProjectionResponse cardProjection(String email, String cardNickname, LocalDate projectionUntilDate) {
        UserCard userCard = userCardRepository.findByNicknameAndUser_EmailAndActiveIsTrue(cardNickname, email)
                .orElseThrow(EntityExistsException::new);

        return ProjectionResponse.builder()
                .initDate(DateUtils.outputFormatDate(LocalDate.now()))
                .projectionUntil(DateUtils.outputFormatDate(projectionUntilDate))
                .debtRowProjection(this.buildDebtRow(userCard, projectionUntilDate))
                .build();
    }

    public ProjectionResponse allDebtProjectionByDate(String email, LocalDate projectionUntilDate) {
        List<UserCard> userCards = userCardRepository.findAllByUser_EmailAndActiveIsTrue(email);

        // Building debts row
        List<DebtRowProjectionResponse> debtsRow = userCards.stream().map(userCard -> this.buildDebtRow(userCard, projectionUntilDate))
                .collect(Collectors.toList());

        return ProjectionResponse.builder()
                .initDate(DateUtils.outputFormatDate(LocalDate.now()))
                .projectionUntil(DateUtils.outputFormatDate(projectionUntilDate))
                .debtRowsProjection(debtsRow)
                .build();
    }
}
