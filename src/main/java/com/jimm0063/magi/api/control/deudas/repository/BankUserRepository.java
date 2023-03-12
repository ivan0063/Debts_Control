package com.jimm0063.magi.api.control.deudas.repository;

import com.jimm0063.magi.api.control.deudas.entity.BankUser;
import com.jimm0063.magi.api.control.deudas.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BankUserRepository extends CrudRepository<BankUser, UUID> {
    List<BankUser> findAllByUserAndActiveIsTrue(User user);
}
