package com.monitoring.app.controller;

import com.monitoring.app.dto.MonitoredEndpointDto;
import com.monitoring.app.model.MonitoredEndpoint;
import com.monitoring.app.model.User;
import com.monitoring.app.repository.MonitoredEndpointRepository;
import com.monitoring.app.service.MonitoredEndpointService;
import com.monitoring.app.service.MonitoringResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/endpoints")
@RequiredArgsConstructor
public class MonitoredEndpointController {

    private final MonitoredEndpointService monitoredEndpointService;
    private final MonitoringResultService monitoringResultService;
    private final MonitoredEndpointRepository monitoredEndpointRepository;

    /**
     * Get all endpoints for authenticated user
     */
    @GetMapping
    public ResponseEntity<List<MonitoredEndpointDto>> getAllEndpoints(HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        List<MonitoredEndpointDto> endpoints = monitoredEndpointService.getAllEndpointsForUser(user);
        return ResponseEntity.ok(endpoints);
    }

    /**
     * Get specific endpoint by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<MonitoredEndpointDto> getEndpointById(
            @PathVariable Long id, HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        MonitoredEndpointDto endpoint = monitoredEndpointService.getEndpointByIdForUser(id, user);
        return ResponseEntity.ok(endpoint);
    }

    /**
     * Create a new endpoint
     */
    @PostMapping
    public ResponseEntity<MonitoredEndpointDto> createEndpoint(
            @Valid @RequestBody MonitoredEndpointDto endpointDto, HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        MonitoredEndpointDto createdEndpoint = monitoredEndpointService.createEndpoint(endpointDto, user);
        return new ResponseEntity<>(createdEndpoint, HttpStatus.CREATED);
    }

    /**
     * Update an existing endpoint
     */
    @PutMapping("/{id}")
    public ResponseEntity<MonitoredEndpointDto> updateEndpoint(
            @PathVariable Long id, 
            @Valid @RequestBody MonitoredEndpointDto endpointDto,
            HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        MonitoredEndpointDto updatedEndpoint = monitoredEndpointService.updateEndpoint(id, endpointDto, user);
        return ResponseEntity.ok(updatedEndpoint);
    }

    /**
     * Delete an endpoint
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEndpoint(
            @PathVariable Long id, HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        monitoredEndpointService.deleteEndpoint(id, user);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get monitoring results for an endpoint
     */
    @GetMapping("/{id}/results")
    public ResponseEntity<?> getMonitoringResults(
            @PathVariable Long id, HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        
        // Verify the endpoint belongs to the user
        monitoredEndpointService.getEndpointByIdForUser(id, user);
        
        // Fetch the actual entity from repository
        MonitoredEndpoint endpoint = monitoredEndpointRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Endpoint not found with id: " + id));
        
        return ResponseEntity.ok(monitoringResultService.getLast10ResultsForEndpoint(endpoint));
    }
} 