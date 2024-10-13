package com.techgirl.spring_auth_jwt.repository;

import com.techgirl.spring_auth_jwt.model.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<MyUser, Integer> {

    Optional<MyUser> findByEmail(String email);
    Optional<MyUser> findByUsername(String email);
}
