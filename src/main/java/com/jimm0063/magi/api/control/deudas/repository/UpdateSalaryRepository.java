package com.jimm0063.magi.api.control.deudas.repository;

import com.jimm0063.magi.api.control.deudas.entity.UpdateSalary;
import jakarta.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UpdateSalaryRepository extends CrudRepository<UpdateSalary, Integer> {
    List<UpdateSalary> findAllByUser_Email(String email);
}
