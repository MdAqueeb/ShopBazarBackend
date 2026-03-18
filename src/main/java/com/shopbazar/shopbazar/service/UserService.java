package com.shopbazar.shopbazar.service;

import com.shopbazar.shopbazar.entity.Role;
import com.shopbazar.shopbazar.entity.User;
import com.shopbazar.shopbazar.exception.ResourceNotFoundException;
import com.shopbazar.shopbazar.repository.RoleRepository;
import com.shopbazar.shopbazar.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    }

    public User updateUserProfile(Long userId, User userDetails) {
        User existing = getUserById(userId);
        if (userDetails.getFirstName() != null) existing.setFirstName(userDetails.getFirstName());
        if (userDetails.getLastName() != null) existing.setLastName(userDetails.getLastName());
        if (userDetails.getEmail() != null) existing.setEmail(userDetails.getEmail());
        if (userDetails.getPhone() != null) existing.setPhone(userDetails.getPhone());
        return userRepository.save(existing);
    }

    public void deleteUser(Long userId) {
        User existing = getUserById(userId);
        userRepository.delete(existing);
    }

    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public User updateUserStatus(Long userId, User.Status status) {
        User existing = getUserById(userId);
        existing.setStatus(status);
        return userRepository.save(existing);
    }

    public User updateUserRole(Long userId, Long roleId) {
        User existing = getUserById(userId);
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + roleId));
        existing.setRole(role);
        return userRepository.save(existing);
    }

    public User blockUser(Long userId, String reason) {
        User user = getUserById(userId);
        user.setStatus(User.Status.BLOCKED);
        user.setBlockReason(reason);
        return userRepository.save(user);
    }

    public User unblockUser(Long userId) {
        User user = getUserById(userId);
        user.setStatus(User.Status.ACTIVE);
        user.setBlockReason(null);
        return userRepository.save(user);
    }

    public Page<User> getUsersByStatus(User.Status status, Pageable pageable) {
        return userRepository.findByStatus(status, pageable);
    }
}
