package com.techgirl.spring_auth_jwt.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "auth_user")
public class MyUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String email;
    private String username;
    private String password;
    private String role;

}
