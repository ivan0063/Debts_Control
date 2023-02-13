package com.jimm0063.magi.api.control.deudas.service;

import com.jimm0063.magi.api.control.deudas.entity.User;
import com.jimm0063.magi.api.control.deudas.entity.UserFixedExpsense;
import com.jimm0063.magi.api.control.deudas.exception.EntityNotFound;
import com.jimm0063.magi.api.control.deudas.models.response.ApiResponse;
import com.jimm0063.magi.api.control.deudas.models.response.StreamingResponseModel;
import com.jimm0063.magi.api.control.deudas.repository.UserFixedExpsenseRepository;
import com.jimm0063.magi.api.control.deudas.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExpenseService {
    private final UserFixedExpsenseRepository userFixedExpsenseRepository;
    private final UserRepository userRepository;

    public ExpenseService(UserFixedExpsenseRepository userFixedExpsenseRepository, UserRepository userRepository) {
        this.userFixedExpsenseRepository = userFixedExpsenseRepository;
        this.userRepository = userRepository;
    }

    public ApiResponse getAllStreamingExpensesByUsr(String email) throws EntityNotFound {
        User user = userRepository.findFristByEmail(email)
                .orElseThrow(EntityNotFound::new);

        List<UserFixedExpsense> userFixedExpenses = userFixedExpsenseRepository.findAllByUserAndFixedExpense_TypeAndActiveIsTrue(user, "Streaming");

        // Calculating the total
        Double streamingTotalAmount = userFixedExpenses.stream()
                .mapToDouble(UserFixedExpsense::getAmount)
                .sum();

        List<Map<String, Object>> streamingExpenses =  userFixedExpenses.stream()
                .map(userFixedExpense -> {
                    Map<String, Object> streaming = new HashMap<>();

                    streaming.put("serviceName", userFixedExpense.getFixedExpense().getExpenseName());
                    streaming.put("payDay", userFixedExpense.getPaymentDay());
                    streaming.put("monthlyAmount", userFixedExpense.getAmount());

                    return streaming;
                })
                .collect(Collectors.toList());

        return ApiResponse.builder()
                .responseMessage("Straming Services")
                .responseObject(
                        StreamingResponseModel.builder()
                                .email(user.getEmail())
                                .streamingServices(streamingExpenses)
                                .totalAmount(streamingTotalAmount)
                                .build()
                )
                .build();
    }

    public Map<String, Object> getNextFixedExpensesByUser(String email) {
        int payDay = (LocalDate.now().getDayOfMonth() <= 15) ? 15 : 30;

        List<UserFixedExpsense> userFixedExpsenses = userFixedExpsenseRepository
                .findAllByUser_EmailAndPaymentDayIsLessThanEqualAndActiveIsTrue(email, payDay)
                .stream()
                .filter(userFixedExpsense -> !(userFixedExpsense.getFixedExpense().getType().equals("Streaming")))
                .collect(Collectors.toList());


        List<Map<String, Object>> listNextFixedExpenses = userFixedExpsenses
                .parallelStream()
                .map(userFixedExpsense -> {
                    Map<String, Object> expense = new HashMap<>();

                    expense.put("serviceName", userFixedExpsense.getFixedExpense().getExpenseName());
                    expense.put("payDay", userFixedExpsense.getPaymentDay());
                    expense.put("monthlyAmount", userFixedExpsense.getAmount());

                    return expense;
                })
                .collect(Collectors.toList());


        Double totalFixedExpensePayment = userFixedExpsenses.parallelStream()
                .mapToDouble(UserFixedExpsense::getAmount)
                .sum();

        if(payDay == 15) {
            Double streamingCost = userFixedExpsenseRepository.findAllByUser_EmailAndFixedExpense_TypeAndActiveIsTrue(email, "Streaming")
                    .stream()
                    .mapToDouble(UserFixedExpsense::getAmount)
                    .sum();

            Map<String, Object> expense = new HashMap<>();
            expense.put("serviceName", "Streaming Services");
            expense.put("payDay", 15);
            expense.put("monthlyAmount", streamingCost);
            listNextFixedExpenses.add(expense);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("nextFixedExpenses", listNextFixedExpenses);
        response.put("total", totalFixedExpensePayment);


        return response;
    }
}
