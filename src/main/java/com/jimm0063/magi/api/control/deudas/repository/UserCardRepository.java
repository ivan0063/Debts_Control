package com.jimm0063.magi.api.control.deudas.repository;

import com.jimm0063.magi.api.control.deudas.entity.User;
import com.jimm0063.magi.api.control.deudas.entity.UserCard;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserCardRepository extends CrudRepository<UserCard, Integer> {
    List<UserCard> findAllByUserAndActiveIsTrue(User user);

    List<UserCard> findAllByUser_EmailAndActiveIsTrue(String email);

    Optional<UserCard> findByNicknameAndUser_EmailAndActiveIsTrue(String nickname, String email);

    List<UserCard> findAllByCard_Bank_BankNameAndUser_EmailAndActiveIsTrue(String bankName, String email);
}
