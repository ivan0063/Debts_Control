package com.jimm0063.magi.api.control.deudas.repository;

import com.jimm0063.magi.api.control.deudas.entity.Capital;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CapitalRepository extends CrudRepository<Capital, UUID> {
    Optional<Capital> findByCapitalName(String capitalName);
}
