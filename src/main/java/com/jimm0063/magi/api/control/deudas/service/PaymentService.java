package com.jimm0063.magi.api.control.deudas.service;

import com.jimm0063.magi.api.control.deudas.entity.Payment;
import com.jimm0063.magi.api.control.deudas.entity.UserCard;
import com.jimm0063.magi.api.control.deudas.exception.EntityNotFound;
import com.jimm0063.magi.api.control.deudas.models.response.CardPaymentsResponse;
import com.jimm0063.magi.api.control.deudas.models.response.UserPaymentResponse;
import com.jimm0063.magi.api.control.deudas.repository.PaymentRepository;
import com.jimm0063.magi.api.control.deudas.repository.UserCardRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
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

    public CardPaymentsResponse getPaymentsMadeByUser(String cardNickname, String email) throws EntityNotFound {
        UserCard userCard = userCardRepository.findByNicknameAndUser_EmailAndActiveIsTrue(cardNickname, email)
                .orElseThrow(EntityNotFound::new);

        List<LocalDateTime> payments = paymentRepository.findAllByUserCardAndActiveIsTrueOrderByPaymentDateDesc(userCard)
                .stream()
                .map(Payment::getPaymentDate)
                .map(Timestamp::toLocalDateTime)
                .collect(Collectors.toList());

        return CardPaymentsResponse.builder()
                .cardBank(userCard.getNickname())
                .payments(payments)
                .lastPaymentMade(payments.size() > 0 ? payments.get(0) : null)
                .build();
    }
}
