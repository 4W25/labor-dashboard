package com.tsmc.labor_manpower_api.service.impl;

import com.tsmc.labor_manpower_api.dto.request.LoginRequest;
import com.tsmc.labor_manpower_api.dto.response.JwtResponse;
// import com.tsmc.labor_manpower_api.model.entity.User; // Not directly needed here anymore
import com.tsmc.labor_manpower_api.repository.UserRepository; // Keep if any direct user ops remain, though not for login core
import com.tsmc.labor_manpower_api.security.JwtTokenProvider;
import com.tsmc.labor_manpower_api.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder; // To ensure it's here for other potential uses or if needed by AuthManager implicitly
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;


@Service
public class AuthServiceImpl implements AuthService {

    // private final UserRepository userRepository; // No longer directly needed for password check
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder; // Retained for completeness, though AuthenticationManager handles encoding checks

    @Autowired
    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           JwtTokenProvider jwtTokenProvider,
                           UserRepository userRepository, // Still can be useful for other user related tasks
                           PasswordEncoder passwordEncoder) {
        // this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public JwtResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenProvider.generateToken(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                                     .map(GrantedAuthority::getAuthority)
                                     .collect(Collectors.toList());

        return new JwtResponse(jwt, userDetails.getUsername(), roles);
    }
}
