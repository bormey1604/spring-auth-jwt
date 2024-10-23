package com.techgirl.user_service.model.request;

import lombok.Data;

@Data
public class ForgotPasswordRequest {
    private String email;
}
