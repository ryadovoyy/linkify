package com.ryadovoy.linkify.repository;

import com.ryadovoy.linkify.model.Role;
import com.ryadovoy.linkify.model.RoleName;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RoleRepository extends CrudRepository<Role, Long> {
    Optional<Role> findByName(RoleName roleName);
}
