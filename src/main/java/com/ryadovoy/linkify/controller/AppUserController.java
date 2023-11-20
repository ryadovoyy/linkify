package com.ryadovoy.linkify.controller;

import com.ryadovoy.linkify.dto.request.LoginRequest;
import com.ryadovoy.linkify.dto.request.RegistrationRequest;
import com.ryadovoy.linkify.dto.response.ApiResponse;
import com.ryadovoy.linkify.dto.response.TokenResponse;
import com.ryadovoy.linkify.service.AppUserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class AppUserController {
    private final AppUserService appUserService;

    @PostMapping("/register")
    public ApiResponse register(@Valid @RequestBody RegistrationRequest registrationRequest) {
        appUserService.registerUser(registrationRequest);
        return new ApiResponse("User registered successfully");
    }

    @PostMapping("/login")
    public TokenResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        String token = appUserService.authenticateUser(loginRequest);
        return new TokenResponse(token, "User logged in successfully");
    }
}
