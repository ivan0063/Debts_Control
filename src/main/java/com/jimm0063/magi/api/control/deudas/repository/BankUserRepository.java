package com.jimm0063.magi.api.control.deudas.repository;

import com.jimm0063.magi.api.control.deudas.entity.BankUser;
import com.jimm0063.magi.api.control.deudas.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankUserRepository extends CrudRepository<BankUser, Integer> {
    List<BankUser> findAllByUserAndActiveIsTrue(User user);
}
