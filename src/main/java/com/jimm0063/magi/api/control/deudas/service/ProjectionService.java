package com.jimm0063.magi.api.control.deudas.service;

import com.jimm0063.magi.api.control.deudas.entity.UserCard;
import com.jimm0063.magi.api.control.deudas.entity.UserFixedExpsense;
import com.jimm0063.magi.api.control.deudas.models.process.projection.DebtMonthProjection;
import com.jimm0063.magi.api.control.deudas.models.request.ProjectionRequest;
import com.jimm0063.magi.api.control.deudas.models.response.projection.BankProjection;
import com.jimm0063.magi.api.control.deudas.models.response.projection.CardProjection;
import com.jimm0063.magi.api.control.deudas.models.response.projection.ProjectionResponse;
import com.jimm0063.magi.api.control.deudas.repository.*;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


@Service
@Slf4j
public class ProjectionService {
    private final UserCardRepository userCardRepository;
    private final UserRepository userRepository;
    private final DebtRepository debtRepository;
    private final PaymentRepository paymentRepository;
    private final CapitalUserRepository capitalUserRepository;
    private final UserFixedExpsenseRepository userFixedExpsenseRepository;
    private final CardService cardService;

    public ProjectionService(UserCardRepository userCardRepository, UserRepository userRepository,
                             DebtRepository debtRepository, PaymentRepository paymentRepository,
                             CapitalUserRepository capitalUserRepository,
                             UserFixedExpsenseRepository userFixedExpsenseRepository, CardService cardService) {
        this.userCardRepository = userCardRepository;
        this.userRepository = userRepository;
        this.debtRepository = debtRepository;
        this.paymentRepository = paymentRepository;
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
                                if (projectionResponse == null) {
                                    projectionResponse = ProjectionResponse.builder()
                                            .monthlyDebtPayment(monthDebtProjection.getMonthTotal())
                                            .extraMonthSaving(0.0)
                                            .savingsTotal(0.0)
                                            .debts(new ArrayList<>())
                                            .build();
                                } else {
                                    double monthlyDebtPayment = projectionResponse.getMonthlyDebtPayment() + monthDebtProjection.getMonthTotal();
                                    projectionResponse.setMonthlyDebtPayment(monthlyDebtPayment);
                                    projectionResponse.getDebts().addAll(monthDebtProjection.getDebtsMonthStatus());
                                }

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
