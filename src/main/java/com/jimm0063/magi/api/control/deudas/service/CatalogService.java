package com.jimm0063.magi.api.control.deudas.service;

import com.jimm0063.magi.api.control.deudas.models.response.FixedExpenseResponse;
import com.jimm0063.magi.api.control.deudas.repository.UserFixedExpenseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CatalogService {
    private final UserFixedExpenseRepository userFixedExpenseRepository;

    public CatalogService(UserFixedExpenseRepository userFixedExpenseRepository) {
        this.userFixedExpenseRepository = userFixedExpenseRepository;
    }

    public List<FixedExpenseResponse> getAllFixedExpenses(String email) {
        return userFixedExpenseRepository.findAllByUser_EmailAndActiveIsTrue(email)
                .stream()
                .map(userFixedExpense -> FixedExpenseResponse.builder()
                            .expenseName(userFixedExpense.getFixedExpense().getExpenseName())
                            .company(userFixedExpense.getFixedExpense().getCompany())
                            .amount(userFixedExpense.getAmount())
                            .paymentDate(userFixedExpense.getPaymentDay())
                            .build()
                )
                .collect(Collectors.toList());
    }
}
