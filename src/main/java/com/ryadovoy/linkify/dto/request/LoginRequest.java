package com.ryadovoy.linkify.dto.request;

import com.ryadovoy.linkify.validation.annotation.AuthEmail;
import com.ryadovoy.linkify.validation.annotation.AuthPassword;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginRequest {
    @AuthEmail
    private String email;

    @AuthPassword
    private String password;
}
