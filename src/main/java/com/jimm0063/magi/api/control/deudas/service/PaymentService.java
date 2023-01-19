package com.jimm0063.magi.api.control.deudas.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jimm0063.magi.api.control.deudas.entity.Payment;
import com.jimm0063.magi.api.control.deudas.models.response.UserPaymentResponse;
import com.jimm0063.magi.api.control.deudas.repository.PaymentRepository;
import com.jimm0063.magi.api.control.deudas.repository.UserCardRepository;
import org.springdoc.webmvc.core.SpringWebMvcProvider;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PaymentService {
    private final UserCardRepository userCardRepository;
    private final PaymentRepository paymentRepository;

    public PaymentService(UserCardRepository userCardRepository, PaymentRepository paymentRepository) {
        this.userCardRepository = userCardRepository;
        this.paymentRepository = paymentRepository;
    }

    public List<UserPaymentResponse> getPaymentsMadeByUser(String email) {
        return userCardRepository.findAllByUser_EmailAndActiveIsTrue(email)
                .stream()
                .map(userCard -> {
                    List<LocalDateTime> paymentsMade = paymentRepository.findAllByUserCardAndActiveIsTrue(userCard)
                            .stream()
                            .map(Payment::getPaymentDate)
                            .map(Timestamp::toLocalDateTime)
                            .collect(Collectors.toList());

                    return UserPaymentResponse.builder()
                            .nickname(userCard.getNickname())
                            .cutOffDay(userCard.getCutOffDay())
                            .paymentsMade(paymentsMade)
                            .build();
                })
                .collect(Collectors.toList());
    }
}
