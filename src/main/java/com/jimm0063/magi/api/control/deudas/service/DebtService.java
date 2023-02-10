package com.jimm0063.magi.api.control.deudas.service;

import com.jimm0063.magi.api.control.deudas.entity.*;
import com.jimm0063.magi.api.control.deudas.exception.EntityNotFound;
import com.jimm0063.magi.api.control.deudas.models.request.DebtReqModel;
import com.jimm0063.magi.api.control.deudas.models.request.MultiDebtReq;
import com.jimm0063.magi.api.control.deudas.models.response.*;
import com.jimm0063.magi.api.control.deudas.repository.BankUserRepository;
import com.jimm0063.magi.api.control.deudas.repository.DebtRepository;
import com.jimm0063.magi.api.control.deudas.repository.UserCardRepository;
import com.jimm0063.magi.api.control.deudas.repository.UserRepository;
import com.jimm0063.magi.api.control.deudas.utils.ModelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DebtService {
    private final DebtRepository debtRepository;
    private final UserRepository userRepository;
    private final UserCardRepository userCardRepository;
    private final BankUserRepository bankUserRepository;

    public DebtService(DebtRepository debtRepository, UserRepository userRepository,
                       UserCardRepository userCardRepository, BankUserRepository bankUserRepository) {
        this.debtRepository = debtRepository;
        this.userRepository = userRepository;
        this.userCardRepository = userCardRepository;
        this.bankUserRepository = bankUserRepository;
    }

    public ApiResponse addDebt(DebtReqModel debtModel) throws EntityNotFound {
        Integer actualInstallment = debtModel.getActualInstallment();
        if (actualInstallment != null)
            actualInstallment = 0;

        Double amountPaid = debtModel.getPaidAmount();
        if (amountPaid == null)
            amountPaid = 0.0;

        UserCard userCard = userCardRepository.findByNicknameAndUser_EmailAndActiveIsTrue(debtModel.getCardNickname(), debtModel.getUser())
                .orElseThrow(EntityNotFound::new);
        Card card = userCard.getCard();
        User user = userCard.getUser();

        // Calculate the end date for the debt
        LocalDate endDate = debtModel.getInitDate().plusMonths(debtModel.getInstallments());
        // Calculating the monthly payment
        Double monthlyPayment = debtModel.getTotalAmount() / debtModel.getInstallments();

        Debt newDebt = new Debt();
        newDebt.setDebtName(debtModel.getDebtName());
        newDebt.setInitDate(debtModel.getInitDate());
        newDebt.setInstallments(debtModel.getInstallments());
        newDebt.setTotalAmount(debtModel.getTotalAmount());
        newDebt.setCurrentInstallment(actualInstallment);
        newDebt.setAmountPaid(amountPaid);
        newDebt.setEndDate(endDate);
        newDebt.setMonthlyPayment(monthlyPayment);
        newDebt.setUserCard(userCard);
        newDebt.setActive(true);

        debtRepository.save(newDebt);

        return ApiResponse.builder()
                .responseMessage("Debt has been created")
                .responseObject(newDebt)
                .build();
    }

    public ApiResponse addMultiDebt(MultiDebtReq multiDebtReq) throws EntityNotFound {
        UserCard userCard = userCardRepository.findByNicknameAndUser_EmailAndActiveIsTrue(multiDebtReq.getCardNickname(), multiDebtReq.getEmail())
                .orElseThrow(EntityNotFound::new);

        multiDebtReq.getDebtData().forEach(debtModel -> {
            // identify tht type of the current installment
            Integer currentInstallment = debtModel.getMonthlyInstallment();
            if (debtModel.getMissingMonthlyPayments() != null && currentInstallment == null) {
                if (debtModel.getDebtDuration() < debtModel.getMissingMonthlyPayments()) {
                    log.error("The Debt duration is less that the already payment months");
                    return;
                }
                currentInstallment = debtModel.getDebtDuration() - debtModel.getMissingMonthlyPayments();
            }

            // Trying to get the purchase date
            LocalDate initDate = LocalDate.now().minusMonths(currentInstallment);
            LocalDate endDate = initDate.plusMonths(debtModel.getDebtDuration());
            // Calculate total amount
            Double totalAmount = debtModel.getMonthlyAmount() * debtModel.getDebtDuration();
            // Paid Amount
            Double amountPaid = currentInstallment * debtModel.getMonthlyAmount();

            Debt newDebt = new Debt();
            newDebt.setDebtName(debtModel.getDebtName());
            newDebt.setInitDate(initDate);
            newDebt.setEndDate(endDate);
            newDebt.setInstallments(debtModel.getDebtDuration());
            newDebt.setTotalAmount(totalAmount);
            newDebt.setCurrentInstallment(currentInstallment);
            newDebt.setAmountPaid(amountPaid);
            newDebt.setMonthlyPayment(debtModel.getMonthlyAmount());
            newDebt.setUserCard(userCard);
            newDebt.setActive(true);

            debtRepository.save(newDebt);
        });

        return ApiResponse.builder()
                .responseMessage("The Debts has been created")
                .build();
    }

    public ApiResponse findAllDebtsByUser(String email) throws EntityNotFound {
        User user = userRepository.findFristByEmail(email)
                .orElseThrow(EntityNotFound::new);

        List<UserCard> cards = userCardRepository.findAllByUserAndActiveIsTrue(user);

        List<BankModelResponse> bankModel = bankUserRepository.findAllByUserAndActiveIsTrue(user)
                .stream()
                .map(BankUser::getBank)
                .map(bank -> {
                    List<CardDebtsModelResponse> modelCards = cards.stream()
                            .filter(userCard -> userCard.getCard() != null)
                            .filter(userCard -> userCard.getCard().getBank().equals(bank))
                            .map(userCard -> {
                                List<DebtModelResponse> debtModelResponseList = debtRepository.findAllByUserCardAndActive(userCard, true)
                                        .stream()
                                        .map(ModelBuilder::buildDebtModelResponse)
                                        .collect(Collectors.toList());

                                return CardDebtsModelResponse.builder()
                                        .cardName(userCard.getNickname())
                                        .debts(debtModelResponseList)
                                        .payDay(userCard.getCutOffDay())
                                        .build();
                            })
                            .collect(Collectors.toList());

                    return BankModelResponse.builder()
                            .cards(modelCards)
                            .bankName(bank.getBankName())
                            .build();
                })
                .collect(Collectors.toList());

        return ApiResponse.builder()
                .responseObject(AllDebtsResponse.builder()
                        .banks(bankModel)
                        .email(user.getEmail())
                        .build()
                )
                .responseMessage("All Debts by user")
                .build();
    }

    public ApiResponse findAllFinishedDebtsByUser(String email) throws EntityNotFound {
        User user = userRepository.findFristByEmail(email)
                .orElseThrow(EntityNotFound::new);

        return ApiResponse.builder()
                .responseMessage("All Debts by user")
                .responseObject(
                        debtRepository.findAllByUserCard_UserAndActive(user, false)
                                .stream()
                                .map(debt -> {
                                    DebtModelResponse debtModelResponse = ModelBuilder.buildDebtModelResponse(debt);
                                    debtModelResponse.setCardNickname(debt.getUserCard().getNickname());
                                    return debtModelResponse;
                                })
                                .collect(Collectors.toList())
                )
                .build();
    }

    public Map<String, Object> findAllByCard(String email, String nickname) throws EntityNotFound {
        UserCard userCard = userCardRepository.findByNicknameAndUser_EmailAndActiveIsTrue(nickname, email)
                .orElseThrow(EntityNotFound::new);

        List<DebtModelResponse> debtsByCard = debtRepository.findAllByUserCardAndActive(userCard, true)
                .stream()
                .map(ModelBuilder::buildDebtModelResponse)
                .collect(Collectors.toList());

        Double monthlyPayment = debtsByCard.parallelStream().mapToDouble(DebtModelResponse::getMonthlyPayment).sum();
        Double total = debtsByCard.parallelStream().mapToDouble(DebtModelResponse::getTotalAmount).sum();

        Map<String, Object> response = new HashMap<>();
        response.put("debts", debtsByCard);
        response.put("total", total);
        response.put("monthlyPayment", monthlyPayment);

        return response;
    }

    public List<DebtModelResponse> debtsAboutToFinish(String email) throws EntityNotFound {
        User user = userRepository.findFristByEmail(email)
                        .orElseThrow(EntityNotFound::new);

        LocalDate currentMonth = LocalDate.now();
        LocalDate nextMonth = LocalDate.now().plusMonths(1);

        return debtRepository.findAllByUserCard_UserAndActive(user, true)
                .stream()
                .filter(debt -> debt.getEndDate().getYear() == currentMonth.getYear())
                .filter(debt -> debt.getEndDate().getMonth() == currentMonth.getMonth())
                .map(ModelBuilder::buildDebtModelResponse)
                .collect(Collectors.toList());
    }
}
