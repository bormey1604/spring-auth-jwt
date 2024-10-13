package com.techgirl.spring_auth_jwt.controller;

import com.techgirl.spring_auth_jwt.model.*;
import com.techgirl.spring_auth_jwt.service.JwtService;
import com.techgirl.spring_auth_jwt.service.MyUserDetailsService;
import com.techgirl.spring_auth_jwt.service.OtpService;
import com.techgirl.spring_auth_jwt.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    private OtpService otpService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody MyUser myUser) {

        if (authService.isEmailExists(myUser.getEmail())) {
            return new ResponseEntity<>(
                    new AuthResponse("400", "Email already exists!"),
                    HttpStatus.BAD_REQUEST
            );
        }

        authService.registerUser(myUser);
        return new ResponseEntity<>(
                new AuthResponse("200", "Successful register!"),
                HttpStatus.OK
        );
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginForm userLogin) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLogin.getUsername(), userLogin.getPassword()));

        if(authentication.isAuthenticated()) {
            String token = jwtService.generateToken(myUserDetailsService.loadUserByUsername(userLogin.getUsername()));
            Map<String, Object> map = Map.of("token", token,"expired_in", jwtService.extractExpiration(token));
            return new ResponseEntity<>(
                    new AuthResponse("200","Successful login!",map),
                    HttpStatus.OK
            );
        }
        else {

            return new ResponseEntity<>(
                    new AuthResponse("401","Invalid credentials!"),
                    HttpStatus.UNAUTHORIZED
            );
        }
    }

    @PostMapping("/forgot_password")
    public ResponseEntity<AuthResponse> forgotPassword(@RequestBody ForgotPasswordRequest request) {

        boolean isEmailExists = authService.isEmailExists(request.getEmail());

        if (!isEmailExists) {
            return new ResponseEntity<>(
                    new AuthResponse("400","Email not registered"),
                    HttpStatus.BAD_REQUEST
            );
        }

        String otp = otpService.generateAndSendOtp(request.getEmail());

        return new ResponseEntity<>(
                new AuthResponse("200","OTP sent to email"),
                HttpStatus.OK
        );
    }

    @PostMapping("/verify_otp")
    public ResponseEntity<AuthResponse> verifyOtp(@RequestBody VerifyOtpRequest request) {

        boolean isOtpValid = otpService.verifyOtp(request.getEmail(), request.getOtp());

        if (!isOtpValid) {
            return new ResponseEntity<>(
                    new AuthResponse("400","Invalid OTP"),
                    HttpStatus.BAD_REQUEST
            );
        }

        String verificationId = UUID.randomUUID().toString();
        otpService.storeVerificationId(request.getEmail(), verificationId);

        return new ResponseEntity<>(
                new AuthResponse("200","OTP verified successfully",Map.of("verification_id", verificationId)),
                HttpStatus.OK
        );
    }

    @PostMapping("/reset_password")
    public ResponseEntity<AuthResponse> resetPassword(@RequestBody ResetPasswordRequest request) {

        boolean isValidVerificationId = otpService.verifyVerificationId(request.getEmail(),request.getVerificationId());

        if (!isValidVerificationId) {
            return new ResponseEntity<>(
                    new AuthResponse("400", "Invalid verification ID"),
                    HttpStatus.BAD_REQUEST
            );
        }

        if (!request.getNewPassword().equals(request.getPasswordConfirmation())) {
            return new ResponseEntity<>(
                    new AuthResponse("400", "Passwords do not match"),
                    HttpStatus.BAD_REQUEST
            );
        }

        authService.updatePassword(request.getEmail(), request.getNewPassword());

        return new ResponseEntity<>(
                new AuthResponse("200", "Password reset successfully"),
                HttpStatus.OK
        );
    }


}
