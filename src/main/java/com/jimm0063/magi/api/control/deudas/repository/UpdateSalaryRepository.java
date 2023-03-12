package com.jimm0063.magi.api.control.deudas.repository;

import com.jimm0063.magi.api.control.deudas.entity.UpdateSalary;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UpdateSalaryRepository extends CrudRepository<UpdateSalary, UUID> {
    List<UpdateSalary> findAllByUser_Email(String email);
}
