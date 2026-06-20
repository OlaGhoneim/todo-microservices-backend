package org.example.userservice.service;

import org.example.userservice.entity.Otp;
import org.example.userservice.entity.User;
import org.example.userservice.repository.OtpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class OtpService {

    @Autowired
    private OtpRepository otpRepository;

    public String generateOtp() {
        int otp = 100000 + new Random().nextInt(900000);
        return String.valueOf(otp);
    }

    public void saveOtp(User user, String otpValue) {
        Optional<Otp> existing = otpRepository.findByUser(user);

        Otp otp;
        if (existing.isPresent()) {
            otp = existing.get();
        } else {
            otp = new Otp();
        }

        otp.setOtp(otpValue);
        otp.setUser(user);
        otp.setExpirationTime(LocalDateTime.now().plusMinutes(10));

        otpRepository.save(otp);
    }

    public boolean isOtpValid(User user, String otpValue) {
        Optional<Otp> otpOpt = otpRepository.findByUser(user);

        if (otpOpt.isEmpty()) return false;

        Otp otp = otpOpt.get();
        boolean notExpired = otp.getExpirationTime().isAfter(LocalDateTime.now());
        boolean matches = otp.getOtp().equals(otpValue);

        return notExpired && matches;
    }
}