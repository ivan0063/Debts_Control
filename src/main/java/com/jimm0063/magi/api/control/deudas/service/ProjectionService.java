package com.jimm0063.magi.api.control.deudas.service;

import com.jimm0063.magi.api.control.deudas.entity.UserCard;
import com.jimm0063.magi.api.control.deudas.entity.UserFixedExpsense;
import com.jimm0063.magi.api.control.deudas.models.process.projection.DebtMonthProjection;
import com.jimm0063.magi.api.control.deudas.models.request.ProjectionRequest;
import com.jimm0063.magi.api.control.deudas.models.response.projection.BankProjection;
import com.jimm0063.magi.api.control.deudas.models.response.projection.CardProjection;
import com.jimm0063.magi.api.control.deudas.models.response.projection.ProjectionResponse;
import com.jimm0063.magi.api.control.deudas.repository.CapitalUserRepository;
import com.jimm0063.magi.api.control.deudas.repository.UserCardRepository;
import com.jimm0063.magi.api.control.deudas.repository.UserFixedExpsenseRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


@Service
@Slf4j
public class ProjectionService {
    private final UserCardRepository userCardRepository;
    private final CapitalUserRepository capitalUserRepository;
    private final UserFixedExpsenseRepository userFixedExpsenseRepository;
    private final CardService cardService;

    public ProjectionService(UserCardRepository userCardRepository,
                             CapitalUserRepository capitalUserRepository,
                             UserFixedExpsenseRepository userFixedExpsenseRepository,
                             CardService cardService) {
        this.userCardRepository = userCardRepository;
        this.capitalUserRepository = capitalUserRepository;
        this.userFixedExpsenseRepository = userFixedExpsenseRepository;
        this.cardService = cardService;
    }

    public BankProjection bankProjection(String email, String bankName, LocalDate projectionUntilDate) {
        List<UserCard> userCards = userCardRepository.findAllByCard_Bank_BankNameAndUser_EmailAndActiveIsTrue(bankName, email);
        // Building debts row
        List<CardProjection> cardProjection = userCards.stream()
                .map(userCard -> {
                    List<DebtMonthProjection> debtMonthProjections = cardService.calculateDebtProjectionByCard(userCard, projectionUntilDate);

                    return CardProjection.builder()
                            .cardNickname(userCard.getNickname())
                            .debtMonthProjections(debtMonthProjections)
                            .build();
                })
                .collect(Collectors.toList());

        return BankProjection.builder()
                .bankName(bankName)
                .cardProjections(cardProjection)
                .build();
    }

    public CardProjection cardProjection(String email, String cardNickname, LocalDate projectionUntilDate) {
        UserCard userCard = userCardRepository.findByNicknameAndUser_EmailAndActiveIsTrue(cardNickname, email)
                .orElseThrow(EntityExistsException::new);
        List<DebtMonthProjection> debtMonthProjections = cardService.calculateDebtProjectionByCard(userCard, projectionUntilDate);

        return CardProjection.builder()
                .cardNickname(userCard.getNickname())
                .debtMonthProjections(debtMonthProjections)
                .build();
    }

    public Object financialProjection(ProjectionRequest projectionRequest) {
        double salary = capitalUserRepository.findByUser_EmailAndCapital_CapitalName(projectionRequest.getEmail(), "Sueldo")
                .orElseThrow(EntityNotFoundException::new)
                .getAmount();
        double  savings = capitalUserRepository.findByUser_EmailAndCapital_CapitalName(projectionRequest.getEmail(), "Ahorro")
                .orElseThrow(EntityNotFoundException::new)
                .getAmount();
        double fixedMonthlyPayment = userFixedExpsenseRepository.findAllByUser_EmailAndActiveIsTrue(projectionRequest.getEmail())
                .stream()
                .mapToDouble(UserFixedExpsense::getAmount)
                .sum();
        Map<String, ProjectionResponse> projection = Collections.synchronizedMap(
                new LinkedHashMap<>());

        userCardRepository.findAllByUser_EmailAndActiveIsTrue(projectionRequest.getEmail())
                .forEach(userCard -> cardService.calculateDebtProjectionByCard(userCard, projectionRequest.getProjectionUntil())
                            .forEach(monthDebtProjection -> {
                                ProjectionResponse projectionResponse = projection.get(monthDebtProjection.getMonth());
                                if (projectionResponse == null)
                                    projectionResponse = ProjectionResponse.builder()
                                            .monthlyDebtPayment(0.0)
                                            .extraMonthSaving(0.0)
                                            .savingsTotal(0.0)
                                            //.debts(new ArrayList<>())
                                            .debts(new LinkedHashMap<>())
                                            .build();

                                //projectionResponse.getDebts().addAll(monthDebtProjection.getDebtsMonthStatus());
                                Map<String, Double> debtsTotals = projectionResponse.getDebts();
                                monthDebtProjection.getDebtsMonthStatus()
                                        .forEach(debtMonthStatus -> {
                                            Double currentTotal = debtsTotals.get(debtMonthStatus.getBankName());
                                            if (currentTotal == null) currentTotal = 0.0;

                                            double updatedTotal = currentTotal + debtMonthStatus.getCurrentMonthPayment();
                                            debtsTotals.put(debtMonthStatus.getBankName(), updatedTotal);
                                        });

                                double monthlyDebtPayment = projectionResponse.getMonthlyDebtPayment() + monthDebtProjection.getMonthTotal();
                                projectionResponse.setMonthlyDebtPayment(monthlyDebtPayment);
                                projection.put(monthDebtProjection.getMonth(), projectionResponse);
                            })
                );

        // Calculate savings
        AtomicReference<Double> savingsAtomic = new AtomicReference<>(savings);
        projection.forEach((key, value) -> {
            double debtPayment = value.getMonthlyDebtPayment();
            double extraMonthSaving = (salary - fixedMonthlyPayment) - debtPayment;
            double currentSavings = savingsAtomic.get();
            double savingsTotal = currentSavings + extraMonthSaving;

            value.setExtraMonthSaving(extraMonthSaving);
            value.setSavingsTotal(savingsTotal);
            savingsAtomic.set(savingsTotal);
        });

        return projection;
    }
}
