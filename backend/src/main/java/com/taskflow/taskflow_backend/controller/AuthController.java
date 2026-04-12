package com.taskflow.taskflow_backend.controller;

import com.taskflow.taskflow_backend.dto.LoginRequest;
import com.taskflow.taskflow_backend.dto.RegisterRequest;
import com.taskflow.taskflow_backend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public String register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        return "User registered successfully";
    }

    @PostMapping("/login")
    public String login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

}
