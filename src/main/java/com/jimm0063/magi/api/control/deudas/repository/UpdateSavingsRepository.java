package com.jimm0063.magi.api.control.deudas.repository;

import com.jimm0063.magi.api.control.deudas.entity.UpdateSavings;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UpdateSavingsRepository extends CrudRepository<UpdateSavings, UUID> {
    List<UpdateSavings> findAllByUser_Email(String email);
}
