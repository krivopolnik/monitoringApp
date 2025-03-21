package com.monitoring.app.service;

import com.monitoring.app.dto.MonitoredEndpointDto;
import com.monitoring.app.exception.ResourceNotFoundException;
import com.monitoring.app.model.MonitoredEndpoint;
import com.monitoring.app.model.User;
import com.monitoring.app.repository.MonitoredEndpointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MonitoredEndpointService {

    private final MonitoredEndpointRepository monitoredEndpointRepository;

    /**
     * Get all endpoints for the user
     *
     * @param user the user
     * @return list of endpoints
     */
    public List<MonitoredEndpointDto> getAllEndpointsForUser(User user) {
        return monitoredEndpointRepository.findAllByOwner(user)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Get a specific endpoint by ID for the user
     *
     * @param id   the endpoint ID
     * @param user the user
     * @return the endpoint
     */
    public MonitoredEndpointDto getEndpointByIdForUser(Long id, User user) {
        MonitoredEndpoint endpoint = monitoredEndpointRepository.findByIdAndOwner(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Endpoint not found with id: " + id));
        return convertToDto(endpoint);
    }

    /**
     * Create a new endpoint for the user
     *
     * @param endpointDto the endpoint data
     * @param user        the user
     * @return the created endpoint
     */
    @Transactional
    public MonitoredEndpointDto createEndpoint(MonitoredEndpointDto endpointDto, User user) {
        MonitoredEndpoint endpoint = convertToEntity(endpointDto);
        endpoint.setOwner(user);
        endpoint.setCreationDate(LocalDateTime.now());
        MonitoredEndpoint savedEndpoint = monitoredEndpointRepository.save(endpoint);
        return convertToDto(savedEndpoint);
    }

    /**
     * Update an endpoint
     *
     * @param id          the endpoint ID
     * @param endpointDto the updated endpoint data
     * @param user        the user
     * @return the updated endpoint
     */
    @Transactional
    public MonitoredEndpointDto updateEndpoint(Long id, MonitoredEndpointDto endpointDto, User user) {
        MonitoredEndpoint existingEndpoint = monitoredEndpointRepository.findByIdAndOwner(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Endpoint not found with id: " + id));

        existingEndpoint.setName(endpointDto.getName());
        existingEndpoint.setUrl(endpointDto.getUrl());
        existingEndpoint.setMonitoringInterval(endpointDto.getMonitoringInterval());

        MonitoredEndpoint updatedEndpoint = monitoredEndpointRepository.save(existingEndpoint);
        return convertToDto(updatedEndpoint);
    }

    /**
     * Delete an endpoint
     *
     * @param id   the endpoint ID
     * @param user the user
     */
    @Transactional
    public void deleteEndpoint(Long id, User user) {
        MonitoredEndpoint endpoint = monitoredEndpointRepository.findByIdAndOwner(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Endpoint not found with id: " + id));
        monitoredEndpointRepository.delete(endpoint);
    }

    /**
     * Get all endpoints for monitoring
     *
     * @return list of all endpoints
     */
    public List<MonitoredEndpoint> getAllEndpoints() {
        return monitoredEndpointRepository.findAll();
    }

    /**
     * Update last check date
     *
     * @param endpoint  the endpoint
     * @param checkDate the check date
     */
    @Transactional
    public void updateLastCheckDate(MonitoredEndpoint endpoint, LocalDateTime checkDate) {
        endpoint.setLastCheckDate(checkDate);
        monitoredEndpointRepository.save(endpoint);
    }

    /**
     * Convert entity to DTO
     *
     * @param endpoint the endpoint entity
     * @return the endpoint DTO
     */
    private MonitoredEndpointDto convertToDto(MonitoredEndpoint endpoint) {
        return new MonitoredEndpointDto(
                endpoint.getId(),
                endpoint.getName(),
                endpoint.getUrl(),
                endpoint.getCreationDate(),
                endpoint.getLastCheckDate(),
                endpoint.getMonitoringInterval()
        );
    }

    /**
     * Convert DTO to entity
     *
     * @param endpointDto the endpoint DTO
     * @return the endpoint entity
     */
    private MonitoredEndpoint convertToEntity(MonitoredEndpointDto endpointDto) {
        MonitoredEndpoint endpoint = new MonitoredEndpoint();
        endpoint.setName(endpointDto.getName());
        endpoint.setUrl(endpointDto.getUrl());
        endpoint.setMonitoringInterval(endpointDto.getMonitoringInterval());
        return endpoint;
    }
} 