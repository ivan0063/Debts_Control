package com.jimm0063.magi.api.control.deudas.service;

import com.jimm0063.magi.api.control.deudas.entity.CapitalUser;
import com.jimm0063.magi.api.control.deudas.entity.Debt;
import com.jimm0063.magi.api.control.deudas.entity.User;
import com.jimm0063.magi.api.control.deudas.entity.UserCard;
import com.jimm0063.magi.api.control.deudas.exception.EntityNotFound;
import com.jimm0063.magi.api.control.deudas.models.request.SavingsUpdateRequestModel;
import com.jimm0063.magi.api.control.deudas.models.response.ApiResponse;
import com.jimm0063.magi.api.control.deudas.models.response.UserFinancialStatusResponse;
import com.jimm0063.magi.api.control.deudas.repository.CapitalUserRepository;
import com.jimm0063.magi.api.control.deudas.repository.DebtRepository;
import com.jimm0063.magi.api.control.deudas.repository.UserCardRepository;
import com.jimm0063.magi.api.control.deudas.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final CapitalUserRepository capitalUserRepository;
    private final UserCardRepository userCardRepository;
    private final DebtRepository debtRepository;
    private final UserRepository userRepository;

    public UserService(CapitalUserRepository capitalUserRepository, UserCardRepository userCardRepository, DebtRepository debtRepository, UserRepository userRepository) {
        this.capitalUserRepository = capitalUserRepository;
        this.userCardRepository = userCardRepository;
        this.debtRepository = debtRepository;
        this.userRepository = userRepository;
    }

    public ApiResponse updateUserSavings(SavingsUpdateRequestModel savingsUpdateRequestModel) throws EntityNotFound {
        CapitalUser capitalUser = capitalUserRepository.findByUser_EmailAndCapital_CapitalName(savingsUpdateRequestModel.getEmail(), "Ahorro")
                .orElseThrow(EntityNotFound::new);

        capitalUser.setAmount(savingsUpdateRequestModel.getSavingsAmountUpdate());
        capitalUserRepository.save(capitalUser);

        return ApiResponse.builder()
                .responseMessage("Savings for user has been saved")
                .build();
    }

    public Object financialStatus(String email) throws EntityNotFound {
        UserFinancialStatusResponse.UserFinancialStatusResponseBuilder userFinancialStatusResponseBuilder = UserFinancialStatusResponse.builder();

        User user = userRepository.findFristByEmail(email)
                .orElseThrow(EntityNotFound::new);

        // Calculating the Debt per bank
        List<UserCard> userCards = userCardRepository.findAllByUser_EmailAndActiveIsTrue(email);
        List<Map> userDebtSpecification = new ArrayList<>();
        for(UserCard userCard : userCards) {
            Map<String, Object> bankDebtSpecification = new HashMap<>();
            double bankMonthlyDebt = 0.0;
            double bankTotalDebt = 0.0;
            List<Debt> debtsByBank = debtRepository.findAllByUserCardAndActive(userCard, true);
            for (Debt bankDebt : debtsByBank) {
                bankMonthlyDebt += bankDebt.getMonthlyPayment();
                bankTotalDebt += bankDebt.getTotalAmount() - (bankDebt.getCurrentInstallment() * bankDebt.getMonthlyPayment());
            }
            bankDebtSpecification.put("card_nick_name", userCard.getNickname());
            bankDebtSpecification.put("monthly_payment", bankMonthlyDebt);
            bankDebtSpecification.put("bank_total_debt", bankTotalDebt);
            userDebtSpecification.add(bankDebtSpecification);
        }


        Double totalMonthlyDebtPayment = userDebtSpecification.parallelStream()
                .mapToDouble(usrDebtSpec -> (Double) usrDebtSpec.get("monthly_payment"))
                .sum();

        Double totalDebt = userDebtSpecification.parallelStream()
                .mapToDouble(usrDebtSpec -> (Double) usrDebtSpec.get("bank_total_debt"))
                .sum();

        List<Map> incomes = capitalUserRepository.findAllByUser_EmailAndActivoIsTrue(email)
                .stream()
                .map(capitalUser -> {
                    Map<String, Object> mapCapital = new HashMap<>();
                    mapCapital.put("incomeName", capitalUser.getCapital().getCapitalName());
                    mapCapital.put("Amount", capitalUser.getAmount());

                    return mapCapital;
                })
                .collect(Collectors.toList());

        Integer countDebts = debtRepository.countAllByUserCard_UserAndActiveIsTrue(user);

        userFinancialStatusResponseBuilder.email(email);
        userFinancialStatusResponseBuilder.totalMonthlyDebtPayment(totalMonthlyDebtPayment);
        userFinancialStatusResponseBuilder.incomes(incomes);
        userFinancialStatusResponseBuilder.totalDebt(totalDebt);
        userFinancialStatusResponseBuilder.debtCount(countDebts);
        userFinancialStatusResponseBuilder.debtSpecification(userDebtSpecification);

        return userFinancialStatusResponseBuilder.build();
    }
}
