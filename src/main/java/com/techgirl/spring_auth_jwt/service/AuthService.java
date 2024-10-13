package com.techgirl.spring_auth_jwt.service;

import com.techgirl.spring_auth_jwt.model.MyUser;
import com.techgirl.spring_auth_jwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public void registerUser(MyUser myUser) {
        myUser.setPassword(encoder.encode(myUser.getPassword()));
        userRepository.save(myUser);
    }


    public boolean isEmailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public boolean updatePassword(String email, String newPassword) {
        Optional<MyUser> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            MyUser user = userOptional.get();
            user.setPassword(encoder.encode(newPassword));
            userRepository.save(user);
            return true;
        }

        return false;
    }
}
