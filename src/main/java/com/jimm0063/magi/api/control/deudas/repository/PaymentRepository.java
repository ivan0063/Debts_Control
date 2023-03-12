package com.jimm0063.magi.api.control.deudas.repository;

import com.jimm0063.magi.api.control.deudas.entity.Payment;
import com.jimm0063.magi.api.control.deudas.entity.UserCard;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentRepository extends CrudRepository<Payment, UUID> {
    List<Payment> findAllByUserCardAndActiveIsTrue(UserCard userCard);

    List<Payment> findAllByUserCardAndActiveIsTrueOrderByPaymentDateDesc(UserCard userCard);

    Optional<Payment> findFirstByPaymentDateIsLessThanAndUserCardAndActiveIsTrue(LocalDate today, UserCard userCard);

    @Query("FROM Payment p WHERE MONTH(p.paymentDate) = :month AND YEAR(p.paymentDate) = :year AND p.userCard = :userCard")
    List<Payment> paymentsMadeThisMonth(@Param("month") Integer month, @Param("year") Integer year, @Param("userCard") UserCard userCard);

    boolean existsPaymentByMonthPaymentMadeAndUserCard_NicknameAndActiveIsTrue(String currentMonth, String nickname);
}
