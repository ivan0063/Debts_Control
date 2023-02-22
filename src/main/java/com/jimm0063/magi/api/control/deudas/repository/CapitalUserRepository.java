package com.jimm0063.magi.api.control.deudas.repository;

import com.jimm0063.magi.api.control.deudas.entity.CapitalUser;
import jakarta.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CapitalUserRepository extends CrudRepository<CapitalUser, Integer> {
    Optional<CapitalUser> findByUser_EmailAndCapital_CapitalName(String email, String capitalName);

    List<CapitalUser> findAllByUser_EmailAndActivoIsTrue(String email);
}