package com.ryadovoy.linkify.service;

import com.ryadovoy.linkify.dto.projection.UserSummary;
import com.ryadovoy.linkify.dto.request.RegistrationRequest;
import org.springframework.data.domain.Page;

public interface UserService {
    void registerUser(RegistrationRequest registrationRequest);
    Page<UserSummary> findAllUsersWithRoles(int page, int size);
}
