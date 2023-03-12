package com.jimm0063.magi.api.control.deudas.service;

import com.jimm0063.magi.api.control.deudas.entity.*;
import com.jimm0063.magi.api.control.deudas.exception.EntityNotFound;
import com.jimm0063.magi.api.control.deudas.models.request.SalaryUpdateRequestModel;
import com.jimm0063.magi.api.control.deudas.models.request.SavingsUpdateRequestModel;
import com.jimm0063.magi.api.control.deudas.models.response.*;
import com.jimm0063.magi.api.control.deudas.repository.*;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
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
    private final UpdateSavingsRepository updateSavingsRepository;
    private final UpdateSalaryRepository updateSalaryRepository;
    private final DebtUpdateRepository debtUpdateRepository;

    public UserService(CapitalUserRepository capitalUserRepository, UserCardRepository userCardRepository,
                       DebtRepository debtRepository, UserRepository userRepository,
                       UpdateSavingsRepository updateSavingsRepository, UpdateSalaryRepository updateSalaryRepository,
                       DebtUpdateRepository debtUpdateRepository) {
        this.capitalUserRepository = capitalUserRepository;
        this.userCardRepository = userCardRepository;
        this.debtRepository = debtRepository;
        this.userRepository = userRepository;
        this.updateSavingsRepository = updateSavingsRepository;
        this.updateSalaryRepository = updateSalaryRepository;
        this.debtUpdateRepository = debtUpdateRepository;
    }

    public ApiResponse updateUserSavings(SavingsUpdateRequestModel savingsUpdateRequestModel) throws EntityNotFound {
        CapitalUser capitalUser = capitalUserRepository.findByUser_EmailAndCapital_CapitalName(savingsUpdateRequestModel.getEmail(), "Ahorro")
                .orElseThrow(EntityNotFound::new);

        UpdateSavings updateSavings = new UpdateSavings();
        updateSavings.setUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
        updateSavings.setNewSavingsValue(savingsUpdateRequestModel.getSavingsAmountUpdate());
        updateSavings.setLastSavingValue(capitalUser.getAmount());
        updateSavings.setDescription(savingsUpdateRequestModel.getDescription());
        updateSavings.setUser(capitalUser.getUser());

        capitalUser.setAmount(savingsUpdateRequestModel.getSavingsAmountUpdate());

        capitalUserRepository.save(capitalUser);
        updateSavingsRepository.save(updateSavings);

        return ApiResponse.builder()
                .responseMessage("Savings for user has been saved")
                .build();
    }

    public ApiResponse updateUserSavings(SalaryUpdateRequestModel salaryUpdateRequestModel) throws EntityNotFound {
        CapitalUser capitalUser = capitalUserRepository.findByUser_EmailAndCapital_CapitalName(salaryUpdateRequestModel.getEmail(), "Sueldo")
                .orElseThrow(EntityNotFound::new);

        UpdateSalary updateSalary = new UpdateSalary();
        updateSalary.setUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
        updateSalary.setNewSalaryValue(salaryUpdateRequestModel.getSalaryAmountUpdate());
        updateSalary.setLastSalaryValue(capitalUser.getAmount());
        updateSalary.setDescription(salaryUpdateRequestModel.getDescription());
        updateSalary.setUser(capitalUser.getUser());

        capitalUser.setAmount(salaryUpdateRequestModel.getSalaryAmountUpdate());

        capitalUserRepository.save(capitalUser);
        updateSalaryRepository.save(updateSalary);

        return ApiResponse.builder()
                .responseMessage("Savings for user has been saved")
                .build();
    }

    public UserFinancialStatusResponse financialStatus(String email) throws Exception {
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
                DebtUpdate debtUpdate = debtUpdateRepository.findFirstByDebtAndActiveIsTrueOrderByTimestampDesc(bankDebt)
                        .orElseThrow(Exception::new);
                bankMonthlyDebt += bankDebt.getMonthlyPayment();
                bankTotalDebt += debtUpdate.getAmountPaid();
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

    public Object getCardsByUser(String email) {
        return userCardRepository.findAllByUser_EmailAndActiveIsTrue(email)
                .stream()
                .map(userCard -> UserCardResponse.builder()
                        .bankName(userCard.getCard().getBank().getBankName())
                        .cardName(userCard.getCard().getCardName())
                        .cutOffDay(userCard.getCutOffDay())
                        .nickname(userCard.getNickname())
                        .build())
                .collect(Collectors.toList());
    }

    public UserResponse validateUser(String email) throws EntityNotFound {
        return userRepository.findFristByEmail(email)
                .map(user -> UserResponse.builder()
                            .lastName(user.getLastName())
                            .name(user.getName())
                            .email(user.getEmail())
                            .build()
                )
                .orElseThrow(EntityNotFound::new);
    }

    public List<UpdateSavingsResponse> getSavingsUpdatedByUser(String email) {
        return updateSavingsRepository.findAllByUser_Email(email)
                .stream()
                .map(updateSavings -> UpdateSavingsResponse.builder()
                        .lastSavingValue(updateSavings.getLastSavingValue())
                        .newSavingsValue(updateSavings.getNewSavingsValue())
                        .updateDate(updateSavings.getUpdateDate().toLocalDateTime())
                        .build()
                )
                .collect(Collectors.toList());
    }

    public List<UpdateSalaryResponse> getSalaryUpdatedByUser(String email) {
        return updateSalaryRepository.findAllByUser_Email(email)
                .stream()
                .map(updateSalary -> UpdateSalaryResponse.builder()
                        .lastSalaryValue(updateSalary.getLastSalaryValue())
                        .newSalaryValue(updateSalary.getNewSalaryValue())
                        .updateDate(updateSalary.getUpdateDate().toLocalDateTime())
                        .build()
                )
                .collect(Collectors.toList());
    }
}
