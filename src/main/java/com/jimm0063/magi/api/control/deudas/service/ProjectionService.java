package com.jimm0063.magi.api.control.deudas.service;

import com.jimm0063.magi.api.control.deudas.entity.UserCard;
import com.jimm0063.magi.api.control.deudas.models.process.DebtMonthProjection;
import com.jimm0063.magi.api.control.deudas.models.response.DebtProjectionResponse;
import com.jimm0063.magi.api.control.deudas.repository.DebtRepository;
import com.jimm0063.magi.api.control.deudas.repository.UserCardRepository;
import com.jimm0063.magi.api.control.deudas.utils.DateUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
public class ProjectionService {
    private final UserCardRepository userCardRepository;
    private final DebtRepository debtRepository;

    public ProjectionService(UserCardRepository userCardRepository, DebtRepository debtRepository) {
        this.userCardRepository = userCardRepository;
        this.debtRepository = debtRepository;
    }

    public List<Map<String, Object>> buildProjection(List<UserCard> userCards, LocalDate projectionUntilDate) {
        // Get Months
        List<String> months = DateUtils.getDateList(LocalDate.now(), projectionUntilDate);

        // Finding all debts
        return userCards.stream()
                .map(userCard -> {
                    Map<String, Object> row = new HashMap<>();
                    List<DebtProjectionResponse> debtsProjection = debtRepository.findAllByUserCardAndActive(userCard, true).stream()
                            .map(debt -> {
                                List<DebtMonthProjection> debtMonthProjections = months.stream()
                                        .map(month -> {
                                            DebtMonthProjection debtProjection = null;
                                            Integer index = months.indexOf(month);
                                            double paid = debt.getAmountPaid() + (debt.getMonthlyPayment() * index);
                                            if (paid < debt.getTotalAmount())
                                                debtProjection = new DebtMonthProjection(month, paid);

                                            return debtProjection;
                                        })
                                        .filter(Objects::nonNull)
                                        .collect(Collectors.toList());

                                return DebtProjectionResponse.builder()
                                        .debtName(debt.getDebtName())
                                        .monthsProjection(debtMonthProjections)
                                        .build();
                            })
                            .collect(Collectors.toList());

                    row.put(userCard.getNickname(), debtsProjection);
                    return row;
                })
                .collect(Collectors.toList());
    }

    public List<Map<String, Object>> bankProjection(String email, String bankName, LocalDate projectionUntilDate) {
        List<UserCard> userCards = userCardRepository.findAllByCard_Bank_BankNameAndUser_EmailAndActiveIsTrue(bankName, email);

        return this.buildProjection(userCards, projectionUntilDate);
    }

    public List<Map<String, Object>> allDebtProjectionByDate(String email, LocalDate projectionUntilDate) {
        List<UserCard> userCards = userCardRepository.findAllByUser_EmailAndActiveIsTrue(email);

        return this.buildProjection(userCards, projectionUntilDate);
    }
}
