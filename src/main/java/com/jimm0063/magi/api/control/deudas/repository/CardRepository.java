package com.jimm0063.magi.api.control.deudas.repository;

import com.jimm0063.magi.api.control.deudas.entity.Bank;
import com.jimm0063.magi.api.control.deudas.entity.Card;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CardRepository extends CrudRepository<Card, UUID> {
    Optional<Card> findFirstByCardName(String cardName);

    Optional<Card> findAllByBank(Bank bank);
}
