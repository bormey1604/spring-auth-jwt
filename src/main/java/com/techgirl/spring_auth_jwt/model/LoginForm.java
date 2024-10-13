package com.techgirl.spring_auth_jwt.model;

import lombok.Data;

@Data
public class LoginForm {
    private String username;
    private String password;
}
