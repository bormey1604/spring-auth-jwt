package com.techgirl.spring_auth_jwt.service;

import com.techgirl.spring_auth_jwt.model.Otp;
import com.techgirl.spring_auth_jwt.model.Verification;
import com.techgirl.spring_auth_jwt.repository.OtpRepository;
import com.techgirl.spring_auth_jwt.repository.VerificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
public class OtpService {

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private VerificationRepository verificationRepository;


    public String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    public void sendOtpEmail(String email, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("OTP Code");
        message.setText("Your OTP code is: " + otp);
        emailSender.send(message);
    }

    public String generateAndSendOtp(String email) {
        String otp = generateOtp();
        sendOtpEmail(email, otp);

        Otp otpEntity = new Otp();
        otpEntity.setEmail(email);
        otpEntity.setOtp(otp);
        otpEntity.setCreatedAt(LocalDateTime.now());
        otpEntity.setExpiresAt(LocalDateTime.now().plusMinutes(5)); // Set expiration time (5 minutes from now)
        otpRepository.save(otpEntity);

        return otp;
    }

    public boolean verifyOtp(String email, String otp) {

        Optional<Otp> storedOtpOpt = otpRepository.findByEmailAndOtp(email,otp);

        if (storedOtpOpt.isPresent()) {
            Otp storedOtp = storedOtpOpt.get();
            if (storedOtp.getOtp().equals(otp) && LocalDateTime.now().isBefore(storedOtp.getExpiresAt())) {

                String verificationId = UUID.randomUUID().toString();
                storeVerificationId(email, verificationId);

                return true;
            }
        }
        return false;
    }


    public void storeVerificationId(String email, String verificationId) {
        Verification verification = new Verification();
        verification.setEmail(email);
        verification.setVerificationId(verificationId);
        verificationRepository.save(verification);
    }


    public boolean verifyVerificationId(String email, String verificationId) {
        Optional<Verification> findVerificationId = verificationRepository.findByEmailAndVerificationId(email,verificationId);
        return findVerificationId.get().getVerificationId().equals(verificationId);
    }


}
