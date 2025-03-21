package com.monitoring.app.service;

import com.monitoring.app.dto.MonitoringResultDto;
import com.monitoring.app.model.MonitoredEndpoint;
import com.monitoring.app.model.MonitoringResult;
import com.monitoring.app.repository.MonitoringResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MonitoringResultService {

    private final MonitoringResultRepository monitoringResultRepository;

    /**
     * Save a monitoring result
     *
     * @param endpoint   the monitored endpoint
     * @param statusCode the HTTP status code
     * @param payload    the response payload
     * @return the saved monitoring result
     */
    @Transactional
    public MonitoringResult saveResult(MonitoredEndpoint endpoint, Integer statusCode, String payload) {
        MonitoringResult result = new MonitoringResult();
        result.setMonitoredEndpoint(endpoint);
        result.setCheckDate(LocalDateTime.now());
        result.setStatusCode(statusCode);
        result.setPayload(payload);
        return monitoringResultRepository.save(result);
    }

    /**
     * Get the last 10 monitoring results for an endpoint
     *
     * @param endpoint the monitored endpoint
     * @return list of monitoring results
     */
    public List<MonitoringResultDto> getLast10ResultsForEndpoint(MonitoredEndpoint endpoint) {
        return monitoringResultRepository
                .findTop10ByMonitoredEndpointOrderByCheckDateDesc(endpoint, PageRequest.of(0, 10))
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Convert entity to DTO
     *
     * @param result the monitoring result entity
     * @return the monitoring result DTO
     */
    private MonitoringResultDto convertToDto(MonitoringResult result) {
        return new MonitoringResultDto(
                result.getId(),
                result.getCheckDate(),
                result.getStatusCode(),
                result.getPayload(),
                result.getMonitoredEndpoint().getId()
        );
    }
} 