package com.monitoring.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonitoredEndpointDto {
    private Long id;
    
    @NotBlank(message = "Name is required")
    private String name;
    
    @NotBlank(message = "URL is required")
    private String url;
    
    private LocalDateTime creationDate;
    
    private LocalDateTime lastCheckDate;
    
    @NotNull(message = "Monitoring interval is required")
    @Min(value = 1, message = "Interval must be at least 1 second")
    private Integer monitoringInterval;
} 