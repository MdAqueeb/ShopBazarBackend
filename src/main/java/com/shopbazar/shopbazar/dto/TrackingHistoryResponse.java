package com.shopbazar.shopbazar.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Response containing full tracking history for a shipment")
public class TrackingHistoryResponse {
    @Schema(description = "Shipment ID", example = "1")
    private Long shipmentId;
    @Schema(description = "List of tracking updates")
    private List<TrackingItem> tracking;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "Individual tracking update item")
    public static class TrackingItem {
        @Schema(description = "Tracking status", example = "SHIPPED")
        private String status;
        @Schema(description = "Location of update", example = "Delhi, DL")
        private String location;
        @Schema(description = "Description of update", example = "Handed over to local carrier")
        private String description;
        @Schema(description = "Timestamp of update", example = "2024-03-21T14:30:00")
        private LocalDateTime time;
    }
}
