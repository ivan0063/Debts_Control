package com.jimm0063.magi.api.control.deudas.repository;

import com.jimm0063.magi.api.control.deudas.entity.Debt;
import com.jimm0063.magi.api.control.deudas.entity.User;
import com.jimm0063.magi.api.control.deudas.entity.UserCard;
import jakarta.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional
public interface DebtRepository extends CrudRepository<Debt, Integer> {
    List<Debt> findAllByUserCardAndActive(UserCard user, Boolean active);

    List<Debt> findAllByUserCard_UserAndActive(User user, Boolean active);

    List<Debt> findAllByUserCard_UserAndEndDateAndActiveIsTrue(User u, LocalDate localDate);

    Integer countAllByUserCard_UserAndActiveIsTrue(User user);
}
