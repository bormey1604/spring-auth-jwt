package com.techgirl.spring_auth_jwt.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1")
public class JobController {

    @GetMapping("/")
    public String index(HttpServletRequest request) {
        return "Hello World: "+ request.getSession().getId();
    }
}
