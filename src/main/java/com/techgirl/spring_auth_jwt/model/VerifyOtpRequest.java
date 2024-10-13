package com.techgirl.spring_auth_jwt.model;

import lombok.Data;

@Data
public class VerifyOtpRequest {
    private String email;
    private String otp;
}
