package com.techgirl.spring_auth_jwt.service;

import com.techgirl.spring_auth_jwt.model.CustomUserDetails;
import com.techgirl.spring_auth_jwt.model.MyUser;
import com.techgirl.spring_auth_jwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String userInput) throws UsernameNotFoundException {
        Optional<MyUser> user = userRepository.findByEmail(userInput);

        if (user.isEmpty()) {
            user = userRepository.findByUsername(userInput);
        }

        if (user.isPresent()) {
            var userObj = user.get();

            return new CustomUserDetails(
                    userObj.getUsername(),
                    userObj.getPassword(),
                    userObj.getEmail(),
                    List.of(new SimpleGrantedAuthority("ROLE_" + userObj.getRole()))
            );
        } else {
            throw new UsernameNotFoundException(userInput);
        }
    }

}
