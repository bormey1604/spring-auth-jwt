package com.techgirl.user_service.service;

import com.techgirl.user_service.model.CustomUserDetails;
import com.techgirl.user_service.model.UserModel;
import com.techgirl.user_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<UserModel> findUser = userRepository.findByUsername(username);

        if(findUser.isEmpty()) {
          findUser = userRepository.findByEmail(username);
        }

        if(findUser.isPresent()) {
            return new CustomUserDetails(findUser.get());
        }

        throw new UsernameNotFoundException("User not found with username or email: " + username);
    }
}
