package com.monitoring.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "monitoring_results")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonitoringResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Check date is required")
    @Column(name = "check_date", nullable = false)
    private LocalDateTime checkDate;

    @Column(name = "status_code")
    private Integer statusCode;

    @Column(name = "payload", columnDefinition = "TEXT")
    private String payload;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "endpoint_id", nullable = false)
    private MonitoredEndpoint monitoredEndpoint;

    @PrePersist
    public void prePersist() {
        if (checkDate == null) {
            checkDate = LocalDateTime.now();
        }
    }
} 