package com.ryadovoy.linkify.service.impl;

import com.ryadovoy.linkify.dto.request.LoginRequest;
import com.ryadovoy.linkify.dto.request.RegistrationRequest;
import com.ryadovoy.linkify.entity.AppUser;
import com.ryadovoy.linkify.exception.UserAlreadyExistsException;
import com.ryadovoy.linkify.exception.AuthenticationException;
import com.ryadovoy.linkify.repository.AppUserRepository;
import com.ryadovoy.linkify.service.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@AllArgsConstructor
public class AppUserServiceImpl implements AppUserService {
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;

    @Override
    public void registerUser(RegistrationRequest registrationRequest) {
        String email = registrationRequest.getEmail();

        if (appUserRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException("Registration failed");
        }

        String password = registrationRequest.getPassword();
        String encodedPassword = passwordEncoder.encode(password);

        AppUser user = new AppUser(email, encodedPassword);
        appUserRepository.save(user);
    }

    @Override
    public String authenticateUser(LoginRequest loginRequest) {
        String email = loginRequest.getEmail();

        AppUser user = appUserRepository.findByEmail(email)
                .orElseThrow(AuthenticationException::new);

        String password = loginRequest.getPassword();

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new AuthenticationException();
        }

        JwsHeader jwsHeader = JwsHeader.with(() -> "HS256").build();

        String userId = user.getId().toString();
        Instant now = Instant.now();
        long expiry = 1800L;

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiry))
                .subject(userId)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }
}
