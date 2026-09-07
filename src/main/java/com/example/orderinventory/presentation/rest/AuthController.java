package com.example.orderinventory.presentation.rest;

import com.example.orderinventory.application.auth.AuthApplicationService;
import com.example.orderinventory.application.auth.dto.AuthTokenResponse;
import com.example.orderinventory.application.auth.dto.LoginRequest;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthApplicationService authApplicationService;

    public AuthController(AuthApplicationService authApplicationService) {
        this.authApplicationService = authApplicationService;
    }

    @PostMapping("/login")
    public AuthTokenResponse login(@Valid @RequestBody LoginRequest request) {
        return authApplicationService.login(request);
    }

    @GetMapping("/status")
    public Map<String, String> status() {
        return Map.of("message", "JWT login, filter, and role-based route protection are enabled.");
    }
}
