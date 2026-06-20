package org.example.userservice.service;

import org.example.userservice.entity.*;
import org.example.userservice.model.request.*;
import org.example.userservice.repository.TokenRepository;
import org.example.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private TokenRepository jwtTokenRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private OtpService otpService;

    @Autowired
    private EmailService emailService;

    public AuthenticationResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(), request.getPassword()
                )
        );
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        Map<String, Object> extraClaims = new HashMap<>();
        String jwtToken = jwtService.createToken(user, extraClaims);
        saveUserToken(user, jwtToken);
        return new AuthenticationResponse(jwtToken, request.getEmail());
    }

    public String register(RegisterRequest request) {
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .enabled(false)
                .build();
        userRepository.save(user);

        String otp = otpService.generateOtp();
        otpService.saveOtp(user, otp);
        emailService.sendOtpEmail(user.getEmail(), otp);

        return "Registered! Check your email for OTP.";
    }

    public String activate(String email, String otp) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!otpService.isOtpValid(user, otp))
            throw new RuntimeException("Invalid or expired OTP");
        user.setEnabled(true);
        userRepository.save(user);
        return "Account activated successfully!";
    }

    public String checkToken(String token) {
        try {
            String email = jwtService.extractEmail(token);
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            if (!user.isEnabled())
                throw new RuntimeException("User is disabled");
            return "Token is valid";
        } catch (Exception e) {
            throw new RuntimeException("Invalid token: " + e.getMessage());
        }
    }

    public String forgetPassword(String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String email = jwtService.extractEmail(token);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        String otp = otpService.generateOtp();
        otpService.saveOtp(user, otp);
        emailService.sendOtpEmail(email, otp);
        return "OTP sent to your email";
    }

    public String changePassword(String authHeader, ChangePasswordRequest request) {
        String token = authHeader.replace("Bearer ", "");
        String email = jwtService.extractEmail(token);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!otpService.isOtpValid(user, request.getOtp()))
            throw new RuntimeException("Invalid or expired OTP");
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        return "Password changed successfully";
    }

    public String regenerateOtp(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        String otp = otpService.generateOtp();
        otpService.saveOtp(user, otp);
        emailService.sendOtpEmail(email, otp);
        return "New OTP sent to your email";
    }

    private void saveUserToken(User user, String jwtToken) {
        JwtToken token = JwtToken.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        jwtTokenRepository.save(token);
    }
}