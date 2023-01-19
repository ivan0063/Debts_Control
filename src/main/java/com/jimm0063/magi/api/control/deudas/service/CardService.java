package com.jimm0063.magi.api.control.deudas.service;

import com.jimm0063.magi.api.control.deudas.entity.Debt;
import com.jimm0063.magi.api.control.deudas.entity.Payment;
import com.jimm0063.magi.api.control.deudas.entity.UserCard;
import com.jimm0063.magi.api.control.deudas.exception.EntityNotFound;
import com.jimm0063.magi.api.control.deudas.models.response.DebtModelResponse;
import com.jimm0063.magi.api.control.deudas.models.response.NextPaymentsResponse;
import com.jimm0063.magi.api.control.deudas.repository.DebtRepository;
import com.jimm0063.magi.api.control.deudas.repository.PaymentRepository;
import com.jimm0063.magi.api.control.deudas.repository.UserCardRepository;
import com.jimm0063.magi.api.control.deudas.utils.ModelBuilder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
public class CardService {
    private final UserCardRepository userCardRepository;
    private final DebtRepository debtRepository;
    private final PaymentRepository paymentRepository;

    public CardService(UserCardRepository userCardRepository, DebtRepository debtRepository, PaymentRepository paymentRepository) {
        this.userCardRepository = userCardRepository;
        this.debtRepository = debtRepository;
        this.paymentRepository = paymentRepository;
    }

    public List<DebtModelResponse> doCardPayment(String cardNickname, String email) throws EntityNotFound{
        UserCard userCard = userCardRepository.findByNicknameAndUser_EmailAndActiveIsTrue(cardNickname, email)
                .orElseThrow(EntityNotFound::new);

        List<Debt> updatedDebts = debtRepository.findAllByUserCardAndActive(userCard, true).stream()
                .peek(debt -> {
                    Integer currentInstallment = debt.getCurrentInstallment();
                    debt.setCurrentInstallment(currentInstallment + 1);
                    debt.setAmountPaid(debt.getAmountPaid() + debt.getMonthlyPayment());
                    if(Objects.equals(debt.getCurrentInstallment(), debt.getInstallments()))
                        debt.setActive(false);
                })
                .collect(Collectors.toList());

        debtRepository.saveAll(updatedDebts);

        Payment payment = new Payment();
        payment.setPaymentDate(Timestamp.valueOf(LocalDateTime.now()));
        payment.setUserCard(userCard);
        paymentRepository.save(payment);

        return updatedDebts.stream()
                .map(ModelBuilder::buildDebtModelResponse)
                .collect(Collectors.toList());
    }

    public List<DebtModelResponse> undonePayment(String cardNickname, String email) throws EntityNotFound {
        UserCard userCard = userCardRepository.findByNicknameAndUser_EmailAndActiveIsTrue(cardNickname, email)
                .orElseThrow(EntityNotFound::new);

        List<Debt> updatedDebts = debtRepository.findAllByUserCardAndActive(userCard, true).stream()
                .peek(debt -> {
                    Integer currentInstallment = debt.getCurrentInstallment();
                    debt.setCurrentInstallment(currentInstallment - 1);
                    debt.setAmountPaid(debt.getAmountPaid() - debt.getMonthlyPayment());
                    if(!debt.getActive())
                        debt.setActive(true);
                })
                .collect(Collectors.toList());

        debtRepository.saveAll(updatedDebts);

        Payment payment = paymentRepository
                .findFirstByPaymentDateIsLessThanAndUserCardAndActiveIsTrue(Timestamp.valueOf(LocalDateTime.now()), userCard)
                .orElseThrow(EntityNotFound::new);
        payment.setActive(false);
        paymentRepository.save(payment);

        return updatedDebts.stream()
                .map(ModelBuilder::buildDebtModelResponse)
                .collect(Collectors.toList());
    }

    public List<NextPaymentsResponse> findNextPaymentByUser(String email) {
        return userCardRepository.findAllByUser_EmailAndActiveIsTrue(email)
                .stream()
                .filter(userCard -> LocalDate.now().getDayOfMonth() <= 15 ?
                            userCard.getCutOffDay() <= 15 :
                            userCard.getCutOffDay() > 15 && userCard.getCutOffDay() <= 30
                )
                //.filter(userCard -> !paymentRepository.existsPaymentByPaymentDateIsLessThanEqualAndUserCard(Timestamp.valueOf(LocalDateTime.now()), userCard))
                .map(userCard -> {
                    Double msiCardPayment = debtRepository.findAllByUserCardAndActive(userCard, true)
                            .stream()
                            .mapToDouble(Debt::getMonthlyPayment)
                            .sum();

                    return NextPaymentsResponse.builder()
                            .bankName(userCard.getNickname())
                            .payday(userCard.getCutOffDay())
                            .msiPayment(msiCardPayment)
                            .build();
                })
                .collect(Collectors.toList());
    }
}