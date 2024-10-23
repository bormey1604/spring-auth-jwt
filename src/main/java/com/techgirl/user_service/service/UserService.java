package com.techgirl.user_service.service;


import com.techgirl.user_service.model.CustomUserDetails;
import com.techgirl.user_service.model.request.LoginRequest;
import com.techgirl.user_service.model.UserModel;
import com.techgirl.user_service.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {


    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    public UserService(UserRepository userRepository, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public boolean isEmailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public void registerUser(UserModel user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public String login(LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),loginRequest.getPassword()));

        if(authentication.isAuthenticated()){
            CustomUserDetails customUserDetail = (CustomUserDetails) authentication.getPrincipal();
            return jwtService.generateToken(customUserDetail);
        }

        return null;
    }

    public boolean updatePassword(String email, String newPassword) {
        Optional<UserModel> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            UserModel user = userOptional.get();
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            return true;
        }

        return false;
    }

}
