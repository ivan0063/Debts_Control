package com.jimm0063.magi.api.control.deudas.repository;

import com.jimm0063.magi.api.control.deudas.entity.UserFixedExpsense;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserFixedExpenseRepository extends CrudRepository<UserFixedExpsense, Integer> {
    List<UserFixedExpsense> findAllByUser_EmailAndActiveIsTrue(String email);
}
