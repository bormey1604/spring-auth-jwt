package com.techgirl.spring_auth_jwt.service;

import com.techgirl.spring_auth_jwt.model.User;
import com.techgirl.spring_auth_jwt.model.UserPrincipal;
import com.techgirl.spring_auth_jwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if(user == null) {
            System.out.println("user = " + user + "not found");
            throw new UsernameNotFoundException(username + " not found");
        }

        return new UserPrincipal(user);
    }
}
