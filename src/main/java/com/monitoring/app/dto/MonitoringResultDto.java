package com.monitoring.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonitoringResultDto {
    private Long id;
    private LocalDateTime checkDate;
    private Integer statusCode;
    private String payload;
    private Long monitoredEndpointId;
} 