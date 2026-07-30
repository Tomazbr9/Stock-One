package com.tomazbr9.stock_one.controller;

import com.tomazbr9.stock_one.dto.CreateUserRequest;
import com.tomazbr9.stock_one.dto.LoginUserRequest;
import com.tomazbr9.stock_one.dto.RecoveryJwtTokenResponse;
import com.tomazbr9.stock_one.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UUID> createUser(@RequestBody @Valid CreateUserRequest request){
        UUID response = authService.createUser(request);
        return ResponseEntity.created(URI.create("api/auth/" + response)).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<RecoveryJwtTokenResponse> loginUser(@RequestBody @Valid LoginUserRequest request){
        RecoveryJwtTokenResponse response = authService.authenticateUser(request);
        return ResponseEntity.ok(response);
    }

}
