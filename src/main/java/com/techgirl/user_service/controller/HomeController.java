package com.techgirl.user_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/home")
    public String home() {
        return "Hello World";
    }

    @GetMapping("/admin/home")
    public String homeadmin() {
        return "Hello ADMIN";
    }
}
