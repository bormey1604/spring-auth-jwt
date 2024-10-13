package com.techgirl.spring_auth_jwt.repository;

import com.techgirl.spring_auth_jwt.model.Verification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationRepository extends JpaRepository<Verification, Long> {
    Optional<Verification> findByEmailAndVerificationId(String email,String verificationId);
}
