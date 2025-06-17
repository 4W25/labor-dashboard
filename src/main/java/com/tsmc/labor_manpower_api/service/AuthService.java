package com.tsmc.labor_manpower_api.service;
import com.tsmc.labor_manpower_api.dto.request.LoginRequest;
import com.tsmc.labor_manpower_api.dto.response.JwtResponse;

public interface AuthService {
    JwtResponse login(LoginRequest loginRequest);
}
