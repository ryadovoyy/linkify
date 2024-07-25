package com.ryadovoy.linkify.controller;

import com.ryadovoy.linkify.dto.projection.UserSummary;
import com.ryadovoy.linkify.dto.request.LoginRequest;
import com.ryadovoy.linkify.dto.request.RegistrationRequest;
import com.ryadovoy.linkify.dto.response.ApiResponse;
import com.ryadovoy.linkify.dto.response.TokenResponse;
import com.ryadovoy.linkify.service.TokenService;
import com.ryadovoy.linkify.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ApiResponse register(@Valid @RequestBody RegistrationRequest registrationRequest) {
        userService.registerUser(registrationRequest);
        return new ApiResponse("User registered successfully");
    }

    @PostMapping("/login")
    public TokenResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());

        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        String token = tokenService.generateToken(authentication);
        return new TokenResponse(token, "User logged in successfully");
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Page<UserSummary> findAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "50") int size) {
        return userService.findAllUsersWithRoles(page, size);
    }
}
