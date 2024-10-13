package com.techgirl.spring_auth_jwt.repository;

import com.techgirl.spring_auth_jwt.model.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<Otp, Long> {

    Optional<Otp> findByEmailAndOtp(String email,String otp);

}