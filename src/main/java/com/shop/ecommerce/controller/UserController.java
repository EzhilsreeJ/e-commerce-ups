package com.shop.ecommerce.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shop.ecommerce.dto.LoginRequest;
import com.shop.ecommerce.dto.LoginResponse;
import com.shop.ecommerce.dto.RegisterRequest;
import com.shop.ecommerce.dto.UserResponse;
import com.shop.ecommerce.model.User;
import com.shop.ecommerce.security.JwtUtil;
import com.shop.ecommerce.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public UserController(
            UserService userService,
            AuthenticationManager authenticationManager,
            JwtUtil jwtUtil) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(
            @Valid @RequestBody RegisterRequest request) {
        User user = userService.register(request);

        UserResponse response = new UserResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()));

        User user = userService.getByEmail(request.email());
        String token = jwtUtil.generateToken(user.getEmail());

        return new LoginResponse(
                token,
                user.getId(),
                user.getFullName(),
                user.getEmail());
    }
}
