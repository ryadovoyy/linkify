package com.ryadovoy.linkify.repository;

import com.ryadovoy.linkify.entity.AppUser;
import org.springframework.data.repository.CrudRepository;

public interface AppUserRepository extends CrudRepository<AppUser, Long> {
}
