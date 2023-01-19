package com.jimm0063.magi.api.control.deudas.repository;

import com.jimm0063.magi.api.control.deudas.entity.Payment;
import com.jimm0063.magi.api.control.deudas.entity.UserCard;
import jakarta.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface PaymentRepository extends CrudRepository<Payment, Integer> {
    Boolean existsPaymentByPaymentDateIsLessThanEqualAndUserCard(Timestamp today, UserCard userCard);

    List<Payment> findAllByUserCardAndActiveIsTrue(UserCard userCard);

    List<Payment> findAllByUserCardAndActiveIsTrueOrderByPaymentDateDesc(UserCard userCard);

    Optional<Payment> findFirstByPaymentDateIsLessThanAndUserCardAndActiveIsTrue(Timestamp today, UserCard userCard);
}
