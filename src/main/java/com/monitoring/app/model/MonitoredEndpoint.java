package com.monitoring.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "monitored_endpoints")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonitoredEndpoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "URL is required")
    @Column(nullable = false)
    private String url;

    @NotNull(message = "Creation date is required")
    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate;

    @Column(name = "last_check_date")
    private LocalDateTime lastCheckDate;

    @NotNull(message = "Monitoring interval is required")
    @Min(value = 1, message = "Interval must be at least 1 second")
    @Column(name = "monitoring_interval", nullable = false)
    private Integer monitoringInterval;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @OneToMany(mappedBy = "monitoredEndpoint", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MonitoringResult> results = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (creationDate == null) {
            creationDate = LocalDateTime.now();
        }
    }
} 