package com.techgirl.spring_auth_jwt.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1")
public class DemoController {

    @GetMapping("/")
    public String index(HttpServletRequest request) {
        return "Hello World ";
    }

    @GetMapping("/user/home")
    public String userpage(HttpServletRequest request) {
        return "Hello users";
    }

    @GetMapping("/admin/home")
    public String adminpage(HttpServletRequest request) {
        return "Hello admin";
    }
}
