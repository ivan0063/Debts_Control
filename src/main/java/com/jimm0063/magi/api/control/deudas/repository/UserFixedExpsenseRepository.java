package com.jimm0063.magi.api.control.deudas.repository;

import com.jimm0063.magi.api.control.deudas.entity.User;
import com.jimm0063.magi.api.control.deudas.entity.UserFixedExpsense;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserFixedExpsenseRepository extends CrudRepository<UserFixedExpsense, Integer> {
    List<UserFixedExpsense> findAllByUserAndFixedExpense_TypeAndActiveIsTrue(User u, String type);

    List<UserFixedExpsense> findAllByUser_EmailAndFixedExpense_TypeAndActiveIsTrue(String email, String type);

    List<UserFixedExpsense> findAllByUserAndActiveIsTrue(User u);

    List<UserFixedExpsense> findAllByUser_EmailAndActiveIsTrue(String email);

    List<UserFixedExpsense> findAllByUser_EmailAndPaymentDayIsLessThanEqualAndActiveIsTrue(String email, Integer payDay);
}
