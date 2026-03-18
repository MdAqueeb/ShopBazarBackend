package com.shopbazar.shopbazar.repository;

import com.shopbazar.shopbazar.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Page<User> findByStatus(User.Status status, Pageable pageable);
    java.util.Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    java.util.Optional<User> findByVerificationToken(String token);
    java.util.Optional<User> findByResetPasswordToken(String token);
}
