package com.shopbazar.shopbazar.service;

import com.shopbazar.shopbazar.dto.NotificationResponse;
import com.shopbazar.shopbazar.entity.Notification;
import com.shopbazar.shopbazar.exception.ResourceNotFoundException;
import com.shopbazar.shopbazar.repository.NotificationRepository;
import com.shopbazar.shopbazar.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Page<NotificationResponse> getUserNotifications(Long userId, Boolean unreadOnly, Pageable pageable) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        Page<Notification> notifications;
        if (unreadOnly != null && unreadOnly) {
            notifications = notificationRepository.findByUser_UserIdAndIsRead(userId, false, pageable);
        } else {
            notifications = notificationRepository.findByUser_UserId(userId, pageable);
        }

        return notifications.map(this::mapToResponse);
    }

    @Transactional
    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + notificationId));
        notification.setIsRead(true);
        notificationRepository.save(notification);
    }

    @Transactional
    public void deleteNotification(Long notificationId) {
        if (!notificationRepository.existsById(notificationId)) {
            throw new ResourceNotFoundException("Notification not found with id: " + notificationId);
        }
        notificationRepository.deleteById(notificationId);
    }

    private NotificationResponse mapToResponse(Notification notification) {
        return NotificationResponse.builder()
                .notificationId(notification.getNotificationId())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .type(notification.getType())
                .isRead(notification.getIsRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
