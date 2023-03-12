package com.jimm0063.magi.api.control.deudas.repository;

import com.jimm0063.magi.api.control.deudas.entity.CapitalUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CapitalUserRepository extends CrudRepository<CapitalUser, UUID> {
    Optional<CapitalUser> findByUser_EmailAndCapital_CapitalName(String email, String capitalName);

    List<CapitalUser> findAllByUser_EmailAndActivoIsTrue(String email);
}