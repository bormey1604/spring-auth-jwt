package com.techgirl.user_service.repository;

import com.techgirl.user_service.model.OtpModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<OtpModel, Long> {
    Optional<OtpModel> findByEmailAndOtp(String email, String otp);
}