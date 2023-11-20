package com.ryadovoy.linkify.service;

import com.ryadovoy.linkify.dto.request.LoginRequest;
import com.ryadovoy.linkify.dto.request.RegistrationRequest;

public interface AppUserService {
    void registerUser(RegistrationRequest registrationRequest);
    String authenticateUser(LoginRequest loginRequest);
}
