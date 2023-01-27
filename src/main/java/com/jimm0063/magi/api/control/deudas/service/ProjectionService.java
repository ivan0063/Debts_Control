package com.jimm0063.magi.api.control.deudas.service;

import com.jimm0063.magi.api.control.deudas.entity.*;
import com.jimm0063.magi.api.control.deudas.models.process.DebtMonthProjection;
import com.jimm0063.magi.api.control.deudas.models.request.ProjectionRequest;
import com.jimm0063.magi.api.control.deudas.models.response.DebtProjectionResponse;
import com.jimm0063.magi.api.control.deudas.models.response.DebtRowProjectionResponse;
import com.jimm0063.magi.api.control.deudas.models.response.ProjectionResponse;
import com.jimm0063.magi.api.control.deudas.repository.*;
import com.jimm0063.magi.api.control.deudas.utils.DateUtils;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


@Service
public class ProjectionService {
    private final UserCardRepository userCardRepository;
    private final UserRepository userRepository;
    private final DebtRepository debtRepository;
    private final PaymentRepository paymentRepository;
    private final CapitalUserRepository capitalUserRepository;
    private final UserFixedExpsenseRepository userFixedExpsenseRepository;

    public ProjectionService(UserCardRepository userCardRepository, UserRepository userRepository,
                             DebtRepository debtRepository, PaymentRepository paymentRepository,
                             CapitalUserRepository capitalUserRepository, UserFixedExpsenseRepository userFixedExpsenseRepository) {
        this.userCardRepository = userCardRepository;
        this.userRepository = userRepository;
        this.debtRepository = debtRepository;
        this.paymentRepository = paymentRepository;
        this.capitalUserRepository = capitalUserRepository;
        this.userFixedExpsenseRepository = userFixedExpsenseRepository;
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

    public Map<String, Object> financialProjection(ProjectionRequest projectionRequest) {
        User user = userRepository.findFristByEmail(projectionRequest.getEmail())
                        .orElseThrow(EntityNotFoundException::new);

        CapitalUser salary = capitalUserRepository.findByUser_EmailAndCapital_CapitalName(user.getEmail(), "Sueldo")
                .orElseThrow(EntityNotFoundException::new);
        CapitalUser savings = capitalUserRepository.findByUser_EmailAndCapital_CapitalName(user.getEmail(), "Ahorro")
                .orElseThrow(EntityNotFoundException::new);

        Double fixedMonthlyPayment = userFixedExpsenseRepository.findAllByUserAndActiveIsTrue(user)
                                .stream()
                                .mapToDouble(UserFixedExpsense::getAmount)
                                .sum();
        AtomicReference<Double> savingUpdated = new AtomicReference<>(savings.getAmount());

        List<Map<String, Object>> monthRows = DateUtils.getDateList(LocalDate.now(), projectionRequest.getProjectionUntil())
                .stream()
                .map(month -> {
                    Double monthDebtPayment = debtRepository.findAllByUserCard_UserAndActive(user, true)
                            .stream()
                            .mapToDouble(debt -> {
                                double amountPaid = debt.getAmountPaid();
                                // Look for the payments made on that month
                                LocalDate preset = LocalDate.now();
                                List<Payment> paymentsMadeThisMonth = paymentRepository
                                        .paymentsMadeThisMonth(preset.getMonth().getValue(), preset.getYear(), debt.getUserCard());
                                if(paymentsMadeThisMonth.isEmpty()) amountPaid += debt.getMonthlyPayment();

                                return (amountPaid <= debt.getTotalAmount()) ? amountPaid : 0;
                            })
                            .sum();
                    Double extraMonthSaving = salary.getAmount() - fixedMonthlyPayment;
                    savingUpdated.updateAndGet(saving -> saving + extraMonthSaving);

                    Map<String, Object> monthFinancialStatus = new HashMap<>();
                    monthFinancialStatus.put("monthDebtPayment", monthDebtPayment);
                    monthFinancialStatus.put("extraMonthSaving", extraMonthSaving);
                    monthFinancialStatus.put("savingsTotal", savingUpdated.get());

                    Map<String, Object> monthRow = new HashMap<>();
                    monthRow.put("month", month);
                    monthRow.put("monthFinancialStatus", monthFinancialStatus);

                    return monthRow;
                })
                .collect(Collectors.toList());

        Map<String, Object> financialProjection = new HashMap<>();
        financialProjection.put("salary", salary.getAmount());
        financialProjection.put("currentSavings", savings.getAmount());
        financialProjection.put("fixedMonthlyPayment", fixedMonthlyPayment);
        financialProjection.put("monthRows", monthRows);

        return financialProjection;
    }
}
