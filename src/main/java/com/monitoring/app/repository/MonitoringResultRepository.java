package com.monitoring.app.repository;

import com.monitoring.app.model.MonitoredEndpoint;
import com.monitoring.app.model.MonitoringResult;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MonitoringResultRepository extends JpaRepository<MonitoringResult, Long> {
    @Query("SELECT r FROM MonitoringResult r WHERE r.monitoredEndpoint = :endpoint ORDER BY r.checkDate DESC")
    List<MonitoringResult> findTop10ByMonitoredEndpointOrderByCheckDateDesc(
            @Param("endpoint") MonitoredEndpoint endpoint,
            Pageable pageable);
} 