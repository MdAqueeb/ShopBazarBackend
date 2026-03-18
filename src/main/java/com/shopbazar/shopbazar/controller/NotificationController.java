package com.shopbazar.shopbazar.controller;
import com.shopbazar.shopbazar.dto.NotificationResponse;
import com.shopbazar.shopbazar.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Notification", description = "Endpoints for managing user notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(summary = "Get user notifications", description = "Retrieves a paginated list of notifications for a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notifications retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/users/{userId}/notifications")
    public ResponseEntity<Page<NotificationResponse>> getUserNotifications(
            @Parameter(description = "ID of the user", required = true)
            @PathVariable Long userId,
            @Parameter(description = "Filter only unread notifications")
            @RequestParam(required = false) Boolean unreadOnly,
            Pageable pageable) {
        return ResponseEntity.ok(notificationService.getUserNotifications(userId, unreadOnly, pageable));
    }

    @Operation(summary = "Mark as read", description = "Marks a specific notification as read")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notification marked as read"),
            @ApiResponse(responseCode = "404", description = "Notification not found")
    })
    @PutMapping("/notifications/{notificationId}/read")
    public ResponseEntity<Void> markAsRead(
            @Parameter(description = "ID of the notification", required = true)
            @PathVariable Long notificationId) {
        notificationService.markAsRead(notificationId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Delete notification", description = "Removes a notification record")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notification deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Notification not found")
    })
    @DeleteMapping("/notifications/{notificationId}")
    public ResponseEntity<Void> deleteNotification(
            @Parameter(description = "ID of the notification to delete", required = true)
            @PathVariable Long notificationId) {
        notificationService.deleteNotification(notificationId);
        return ResponseEntity.ok().build();
    }
}
