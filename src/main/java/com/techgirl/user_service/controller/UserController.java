package com.techgirl.user_service.controller;


import com.techgirl.user_service.model.request.ForgotPasswordRequest;
import com.techgirl.user_service.model.request.LoginRequest;
import com.techgirl.user_service.model.UserModel;
import com.techgirl.user_service.model.request.ResetPasswordRequest;
import com.techgirl.user_service.model.request.VerifyOtpRequest;
import com.techgirl.user_service.model.response.Response;
import com.techgirl.user_service.service.OtpService;
import com.techgirl.user_service.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class UserController {


    private final UserService userService;
    private final OtpService otpService;

    public UserController(UserService userService, OtpService otpService) {
        this.userService = userService;
        this.otpService = otpService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserModel user){

        boolean isEmailExists = userService.isEmailExists(user.getEmail());
        if (isEmailExists) {
            return new ResponseEntity<>(Response.badRequest("Email already exists"),HttpStatus.BAD_REQUEST);
        }
        userService.registerUser(user);
        return new ResponseEntity<>(Response.success(), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        String token = userService.login(loginRequest);

        if(token == null){
            return new ResponseEntity<>(Response.unauthorized(), HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(Response.success(Map.of("access_token",token)), HttpStatus.OK);

    }

    @PostMapping("/forgot_password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {

        boolean isEmailExists = userService.isEmailExists(request.getEmail());
        if (!isEmailExists) {
            return new ResponseEntity<>(Response.badRequest("Email not registered"),HttpStatus.BAD_REQUEST);
        }
        otpService.generateAndSendOtp(request.getEmail());

        return new ResponseEntity<>(Response.success("OTP has sent to your email"), HttpStatus.OK);

    }

    @PostMapping("/verify_otp")
    public ResponseEntity<?> verifyOtp(@RequestBody VerifyOtpRequest request) {

        boolean isOtpValid = otpService.verifyOtp(request.getEmail(), request.getOtp());
        if (!isOtpValid) {
            return new ResponseEntity<>(Response.badRequest("Invalid OTP!"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(Response.success("OTP verified successfully"), HttpStatus.OK);
    }

    @PostMapping("/reset_password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {

        if (!request.getNewPassword().equals(request.getPasswordConfirmation())) {
            return new ResponseEntity<>(Response.badRequest("Passwords do not match"), HttpStatus.BAD_REQUEST);
        }
        userService.updatePassword(request.getEmail(), request.getNewPassword());
        return new ResponseEntity<>(Response.success("Password reset successfully"), HttpStatus.OK);
    }
}
