package com.ryadovoy.linkify.repository;

import com.ryadovoy.linkify.dto.projection.UserSummary;
import com.ryadovoy.linkify.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("select u.id from User u")
    Page<Long> findAllIds(Pageable pageable);

    @EntityGraph(attributePaths = { "roles" })
    List<UserSummary> findWithRolesByIdIn(List<Long> userIds, Sort sort);
}
