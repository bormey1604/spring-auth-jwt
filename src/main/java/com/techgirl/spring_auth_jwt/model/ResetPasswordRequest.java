package com.techgirl.spring_auth_jwt.model;


import lombok.Data;

@Data
public class ResetPasswordRequest {
    private String email;
    private String newPassword;
    private String passwordConfirmation;
    private String verificationId;

}
