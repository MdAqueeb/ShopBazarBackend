package com.shopbazar.shopbazar.repository;

import com.shopbazar.shopbazar.entity.RefreshToken;
import com.shopbazar.shopbazar.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    @Modifying // Tells Spring this is a DELETE/UPDATE query
    @Transactional
    void deleteByUser(User user);
}
