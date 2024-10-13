package com.techgirl.spring_auth_jwt.model;

import lombok.Data;

@Data
public class ForgotPasswordRequest {
    private String email;
}
