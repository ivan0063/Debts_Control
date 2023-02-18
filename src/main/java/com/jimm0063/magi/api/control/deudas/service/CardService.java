package com.jimm0063.magi.api.control.deudas.service;

import com.jimm0063.magi.api.control.deudas.entity.Debt;
import com.jimm0063.magi.api.control.deudas.entity.Payment;
import com.jimm0063.magi.api.control.deudas.entity.UserCard;
import com.jimm0063.magi.api.control.deudas.exception.EntityNotFound;
import com.jimm0063.magi.api.control.deudas.models.process.projection.DebtMonthProjection;
import com.jimm0063.magi.api.control.deudas.models.process.projection.DebtMonthStatus;
import com.jimm0063.magi.api.control.deudas.models.response.DebtModelResponse;
import com.jimm0063.magi.api.control.deudas.models.response.NextPaymentsResponse;
import com.jimm0063.magi.api.control.deudas.repository.DebtRepository;
import com.jimm0063.magi.api.control.deudas.repository.PaymentRepository;
import com.jimm0063.magi.api.control.deudas.repository.UserCardRepository;
import com.jimm0063.magi.api.control.deudas.utils.DateUtils;
import com.jimm0063.magi.api.control.deudas.utils.ModelBuilder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
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

        List<Debt> updatedDebts = debtRepository.findAllByUserCardAndActive(userCard, true)
                .stream()
                .peek(debt -> {
                    debt.setCurrentInstallment(debt.getCurrentInstallment() + 1);
                    debt.setAmountPaid(debt.getAmountPaid() + debt.getMonthlyPayment());
                    if(Objects.equals(debt.getCurrentInstallment(), debt.getInstallments()))
                        debt.setActive(false);
                })
                .collect(Collectors.toList());

        debtRepository.saveAll(updatedDebts);

        Payment payment = new Payment();
        payment.setPaymentDate(LocalDate.now());
        payment.setUserCard(userCard);
        payment.setMonthPaymentMade(Month.from(LocalDate.now()).toString());
        payment.setActive(true);
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
                .findFirstByPaymentDateIsLessThanAndUserCardAndActiveIsTrue(LocalDate.now(), userCard)
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

    public List<DebtMonthProjection> calculateDebtProjectionByCard(UserCard userCard, LocalDate until) {
        // Getting the debts
        List<Debt> debtsByCard = debtRepository.findAllByUserCardAndActive(userCard, true);

        // Calculating the dates
        debtsByCard.removeAll(debtsByCard.stream()
                .filter(debt ->
                        debt.getEndDate().getMonth() == LocalDate.now().getMonth() &&
                                debt.getEndDate().getYear() == LocalDate.now().getYear())
                .collect(Collectors.toList()));

        debtsByCard = debtsByCard.stream()
                .peek(debt -> {
                    boolean existPayment = paymentRepository.existsPaymentByMonthPaymentMadeAndUserCard_NicknameAndActiveIsTrue(LocalDate.now().getMonth().toString(), debt.getUserCard().getNickname());
                    if(!existPayment) debt.setCurrentInstallment(debt.getCurrentInstallment() + 1);
                })
                .collect(Collectors.toList());

        // Calculating projection
        List<String> months = DateUtils.getDateList(LocalDate.now().plusMonths(1), until);
        List<DebtMonthProjection> debtsMonthProjections = new ArrayList<>();
        AtomicInteger monthIncrement = new AtomicInteger(1);

        for (String month : months) {
            List<DebtMonthStatus> debtMonthStatuses = debtsByCard
                    .stream().map(debt -> {
                        Double monthlyPayment = debt.getMonthlyPayment();
                        int currentInstallment = debt.getCurrentInstallment() + monthIncrement.get();

                        if(currentInstallment > debt.getInstallments())
                            monthlyPayment = 0.0;

                        String currentMonth = currentInstallment +  "/" + debt.getInstallments();

                        return DebtMonthStatus.builder()
                                .currentMonthPayment(monthlyPayment)
                                .debtName(debt.getDebtName())
                                .currentMonth(currentMonth)
                                .build();
                    })
                    .filter(debtMonthStatus -> debtMonthStatus.getCurrentMonthPayment() != 0)
                    .collect(Collectors.toList());

            double monthTotal = debtMonthStatuses.parallelStream()
                    .mapToDouble(DebtMonthStatus::getCurrentMonthPayment)
                    .sum();

            debtsMonthProjections.add(DebtMonthProjection.builder()
                    .debtsMonthStatus(debtMonthStatuses)
                    .month(month)
                    .monthTotal(monthTotal)
                    .build());

            monthIncrement.getAndIncrement();
        }

        return debtsMonthProjections;
    }
}
