package com.shopbazar.shopbazar.repository;

import com.shopbazar.shopbazar.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Page<Notification> findByUser_UserId(Long userId, Pageable pageable);
    Page<Notification> findByUser_UserIdAndIsRead(Long userId, Boolean isRead, Pageable pageable);
}
