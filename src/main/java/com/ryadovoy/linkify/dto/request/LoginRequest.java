package com.ryadovoy.linkify.dto.request;

import com.ryadovoy.linkify.validation.annotation.AuthEmail;
import com.ryadovoy.linkify.validation.annotation.AuthPassword;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @AuthEmail
    private String email;

    @AuthPassword
    private String password;
}
