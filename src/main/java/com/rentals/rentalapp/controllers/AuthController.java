package com.rentals.rentalapp.controllers;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rentals.rentalapp.controllers.dto.LoginRequest;
import com.rentals.rentalapp.controllers.dto.MeResponse;
import com.rentals.rentalapp.controllers.dto.RegisterRequest;
import com.rentals.rentalapp.model.User;
import com.rentals.rentalapp.services.JWTService;
import com.rentals.rentalapp.services.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;
    @Autowired
    private JWTService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        try {
            User newUser = userService.registerUser(registerRequest.getEmail(), registerRequest.getName(),
                    registerRequest.getPassword());

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    newUser.getEmail(),
                    null,
                    Collections.emptyList());

            String token = jwtService.generateToken(authentication);

            Map<String, String> tokenResponse = new HashMap<>();
            tokenResponse.put("token", token);

            return ResponseEntity.ok(tokenResponse);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            String token = jwtService.generateToken(authentication);
            Map<String, String> tokenResponse = new HashMap<>();
            tokenResponse.put("token", token);
            return ResponseEntity.ok(tokenResponse);
        } catch (BadCredentialsException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Invalid credentials");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMe(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        Optional<User> optionalUser = userService.findByEmail(authentication.getName());
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(401).build();
        }
        User user = optionalUser.get();
        MeResponse meResponse = new MeResponse(user.getId(), user.getName(), user.getEmail(), user.getCreatedAt(),
                user.getUpdatedAt());
        return ResponseEntity.ok(meResponse);
    }

}
