package com.jimm0063.magi.api.control.deudas.repository;

import com.jimm0063.magi.api.control.deudas.entity.Debt;
import com.jimm0063.magi.api.control.deudas.entity.DebtUpdate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DebtUpdateRepository extends CrudRepository<DebtUpdate, UUID> {
    Optional<DebtUpdate> findFirstByDebtAndActiveIsTrueOrderByTimestampDesc(Debt debt);
}
