package com.monitoring.app.service;

import com.monitoring.app.model.MonitoredEndpoint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MonitoringService {

    private final MonitoredEndpointService monitoredEndpointService;
    private final MonitoringResultService monitoringResultService;
    private final RestTemplate restTemplate;

    /**
     * Check all monitored endpoints based on their intervals
     */
    @Scheduled(fixedRate = 1000) // Check every second
    public void checkEndpoints() {
        List<MonitoredEndpoint> endpoints = monitoredEndpointService.getAllEndpoints();
        LocalDateTime now = LocalDateTime.now();

        endpoints.forEach(endpoint -> {
            // Check if it's time to monitor this endpoint
            if (shouldCheckEndpoint(endpoint, now)) {
                log.debug("Checking endpoint: {}", endpoint.getUrl());
                checkEndpoint(endpoint, now);
            }
        });
    }

    /**
     * Check a specific endpoint and save the result
     *
     * @param endpoint the endpoint to check
     * @param now      the current time
     */
    private void checkEndpoint(MonitoredEndpoint endpoint, LocalDateTime now) {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(endpoint.getUrl(), String.class);
            
            // Save the monitoring result
            monitoringResultService.saveResult(
                    endpoint,
                    response.getStatusCodeValue(),
                    response.getBody()
            );
            
            // Update the last check date
            monitoredEndpointService.updateLastCheckDate(endpoint, now);
            
        } catch (RestClientException e) {
            log.error("Error monitoring endpoint {}: {}", endpoint.getUrl(), e.getMessage());
            
            // Save error result
            monitoringResultService.saveResult(
                    endpoint,
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    e.getMessage()
            );
            
            // Update the last check date even if there was an error
            monitoredEndpointService.updateLastCheckDate(endpoint, now);
        }
    }

    /**
     * Determine if an endpoint should be checked based on its monitoring interval
     *
     * @param endpoint the endpoint
     * @param now      the current time
     * @return true if the endpoint should be checked
     */
    private boolean shouldCheckEndpoint(MonitoredEndpoint endpoint, LocalDateTime now) {
        LocalDateTime lastCheck = endpoint.getLastCheckDate();
        
        // If never checked, check it now
        if (lastCheck == null) {
            return true;
        }
        
        // Check if the interval has passed
        long secondsSinceLastCheck = java.time.Duration.between(lastCheck, now).getSeconds();
        return secondsSinceLastCheck >= endpoint.getMonitoringInterval();
    }
} 