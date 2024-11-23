package com.zosh.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class HomeController {
    @GetMapping
    public String homePage() {
        return "Welcome to home page";
    }

    @GetMapping("/api")
    public String secure() {
        return "Welcome to secure home page";
    }
}
