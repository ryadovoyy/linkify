package com.ryadovoy.linkify.dto.request;

import com.ryadovoy.linkify.validation.annotation.RegistrationEmail;
import com.ryadovoy.linkify.validation.annotation.RegistrationPassword;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationRequest {
    @RegistrationEmail
    private String email;

    @RegistrationPassword
    private String password;
}
