package com.tsmc.labor_manpower_api.controller;

import com.tsmc.labor_manpower_api.dto.request.LoginRequest;
import com.tsmc.labor_manpower_api.dto.response.JwtResponse;
import com.tsmc.labor_manpower_api.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            JwtResponse jwtResponse = authService.login(loginRequest);
            return ResponseEntity.ok(jwtResponse);
        } catch (RuntimeException e) { // Replace with specific AuthenticationException later
            return ResponseEntity.status(401).body(e.getMessage()); // Unauthorized
        }
    }
}
