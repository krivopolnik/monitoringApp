package com.monitoring.app.repository;

import com.monitoring.app.model.MonitoredEndpoint;
import com.monitoring.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MonitoredEndpointRepository extends JpaRepository<MonitoredEndpoint, Long> {
    List<MonitoredEndpoint> findAllByOwner(User owner);
    Optional<MonitoredEndpoint> findByIdAndOwner(Long id, User owner);
} 