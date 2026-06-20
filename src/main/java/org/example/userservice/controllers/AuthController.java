package org.example.userservice.controllers;

import org.example.userservice.model.request.ChangePasswordRequest;
import org.example.userservice.model.request.LoginRequest;
import org.example.userservice.model.request.RegisterRequest;
import org.example.userservice.model.request.AuthenticationResponse;
import org.example.userservice.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @GetMapping("/activate")
    public ResponseEntity<String> activate(@RequestParam String email,
                                           @RequestParam String otp) {
        return ResponseEntity.ok(authService.activate(email, otp));
    }

    @PostMapping("/checkToken")
    public ResponseEntity<String> checkToken(@RequestParam String token) {
        return ResponseEntity.ok(authService.checkToken(token));
    }

    @PostMapping("/forgetPassword")
    public ResponseEntity<String> forgetPassword(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(authService.forgetPassword(token));
    }

    @PostMapping("/changePassword")
    public ResponseEntity<String> changePassword(
            @RequestHeader("Authorization") String token,
            @RequestBody ChangePasswordRequest request) {
        return ResponseEntity.ok(authService.changePassword(token, request));
    }

    @GetMapping("/regenerateOtp")
    public ResponseEntity<String> regenerateOtp(@RequestParam String email) {
        return ResponseEntity.ok(authService.regenerateOtp(email));
    }
}