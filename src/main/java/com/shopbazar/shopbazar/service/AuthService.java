package com.shopbazar.shopbazar.service;

import com.shopbazar.shopbazar.dto.*;
import com.shopbazar.shopbazar.entity.RefreshToken;
import com.shopbazar.shopbazar.entity.Role;
import com.shopbazar.shopbazar.entity.User;
import com.shopbazar.shopbazar.exception.BadRequestException;
import com.shopbazar.shopbazar.exception.ConflictException;
import com.shopbazar.shopbazar.exception.ResourceNotFoundException;
import com.shopbazar.shopbazar.exception.UnauthorizedException;
import com.shopbazar.shopbazar.repository.RefreshTokenRepository;
import com.shopbazar.shopbazar.repository.RoleRepository;
import com.shopbazar.shopbazar.repository.UserRepository;
import com.shopbazar.shopbazar.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public User register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("Email already exists");
        }

        Role role = roleRepository.findByRoleName(Role.RoleName.CUSTOMER)
                .orElseThrow(() -> new ResourceNotFoundException("Default role not found"));

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .status(User.Status.ACTIVE)
                .verificationToken(UUID.randomUUID().toString())
                .verificationTokenExpiry(LocalDateTime.now().plusHours(24))
                .build();

        return userRepository.save(user);
        // In a real app, send verification email here
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getStatus() == User.Status.BLOCKED) {
            throw new UnauthorizedException("User is blocked");
        }

        var userDetails = new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                java.util.Collections
                        .singletonList(new org.springframework.security.core.authority.SimpleGrantedAuthority(
                                "ROLE_" + user.getRole().getRoleName().name())));

        String accessToken = jwtService.generateToken(userDetails);
        String refreshToken = createRefreshToken(user).getToken();

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getUserId())
                .email(user.getEmail())
                .role(user.getRole().getRoleName().name())
                .build();
    }

    @Transactional
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        return refreshTokenRepository.findByToken(request.getRefreshToken())
                .map(this::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    var userDetails = new org.springframework.security.core.userdetails.User(
                            user.getEmail(),
                            user.getPassword(),
                            java.util.Collections.singletonList(
                                    new org.springframework.security.core.authority.SimpleGrantedAuthority(
                                            "ROLE_" + user.getRole().getRoleName().name())));
                    String accessToken = jwtService.generateToken(userDetails);
                    return AuthResponse.builder()
                            .accessToken(accessToken)
                            .refreshToken(request.getRefreshToken())
                            .userId(user.getUserId())
                            .email(user.getEmail())
                            .role(user.getRole().getRoleName().name())
                            .build();
                })
                .orElseThrow(() -> new BadRequestException("Invalid or expired refresh token"));
    }

    @Transactional
    public void logout(String refreshToken) {
        refreshTokenRepository.findByToken(refreshToken)
                .ifPresent(refreshTokenRepository::delete);
    }

    @Transactional
    public void forgotPassword(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setResetPasswordToken(UUID.randomUUID().toString());
        user.setResetPasswordTokenExpiry(LocalDateTime.now().plusHours(1));
        userRepository.save(user);
        // In a real app, send password reset email here
    }

    @Transactional
    public void resetPassword(String token, ResetPasswordRequest request) {
        User user = userRepository.findByResetPasswordToken(token)
                .filter(u -> u.getResetPasswordTokenExpiry().isAfter(LocalDateTime.now()))
                .orElseThrow(() -> new BadRequestException("Invalid or expired reset token"));

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setResetPasswordToken(null);
        user.setResetPasswordTokenExpiry(null);
        userRepository.save(user);
    }

    @Transactional
    public void verifyEmail(String token) {
        User user = userRepository.findByVerificationToken(token)
                .filter(u -> u.getVerificationTokenExpiry().isAfter(LocalDateTime.now()))
                .orElseThrow(() -> new BadRequestException("Invalid or expired verification token"));

        user.setEmailVerified(true);
        user.setVerificationToken(null);
        user.setVerificationTokenExpiry(null);
        userRepository.save(user);
    }

    private RefreshToken createRefreshToken(User user) {
        refreshTokenRepository.deleteByUser(user);
        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(604800000)) // 7 days
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    private RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new BadRequestException("Refresh token was expired. Please make a new signin request");
        }
        return token;
    }
}
