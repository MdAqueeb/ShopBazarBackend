package com.shopbazar.shopbazar.dto;

import com.shopbazar.shopbazar.entity.Notification;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Response containing notification details")
public class NotificationResponse {
    @Schema(description = "Notification ID", example = "1")
    private Long notificationId;
    @Schema(description = "Notification title", example = "Order Shipped")
    private String title;
    @Schema(description = "Notification message content", example = "Your order #123 has been shipped.")
    private String message;
    @Schema(description = "Notification type", example = "ORDER_UPDATE")
    private Notification.Type type;
    @Schema(description = "Read status", example = "false")
    private Boolean isRead;
    @Schema(description = "Notification creation timestamp", example = "2024-03-22T10:00:00")
    private LocalDateTime createdAt;
}
