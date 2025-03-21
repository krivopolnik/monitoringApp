package com.monitoring.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monitoring.app.dto.MonitoredEndpointDto;
import com.monitoring.app.model.User;
import com.monitoring.app.service.MonitoredEndpointService;
import com.monitoring.app.service.MonitoringResultService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MonitoredEndpointController.class)
public class MonitoredEndpointControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MonitoredEndpointService monitoredEndpointService;

    @MockBean
    private MonitoringResultService monitoringResultService;

    private User testUser;
    private MonitoredEndpointDto testEndpoint;

    @BeforeEach
    public void setup() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("TestUser");
        testUser.setEmail("test@example.com");
        testUser.setAccessToken("test-token");

        testEndpoint = new MonitoredEndpointDto();
        testEndpoint.setId(1L);
        testEndpoint.setName("Test Endpoint");
        testEndpoint.setUrl("https://test.com");
        testEndpoint.setCreationDate(LocalDateTime.now());
        testEndpoint.setMonitoringInterval(60);
    }

    @Test
    public void testGetAllEndpoints() throws Exception {
        when(monitoredEndpointService.getAllEndpointsForUser(any(User.class)))
                .thenReturn(Arrays.asList(testEndpoint));

        mockMvc.perform(get("/api/endpoints")
                        .requestAttr("user", testUser))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test Endpoint"))
                .andExpect(jsonPath("$[0].url").value("https://test.com"));
    }

    @Test
    public void testGetEndpointById() throws Exception {
        when(monitoredEndpointService.getEndpointByIdForUser(eq(1L), any(User.class)))
                .thenReturn(testEndpoint);

        mockMvc.perform(get("/api/endpoints/1")
                        .requestAttr("user", testUser))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Endpoint"))
                .andExpect(jsonPath("$.url").value("https://test.com"));
    }

    @Test
    public void testCreateEndpoint() throws Exception {
        when(monitoredEndpointService.createEndpoint(any(MonitoredEndpointDto.class), any(User.class)))
                .thenReturn(testEndpoint);

        mockMvc.perform(post("/api/endpoints")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testEndpoint))
                        .requestAttr("user", testUser))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test Endpoint"))
                .andExpect(jsonPath("$.url").value("https://test.com"));
    }

    @Test
    public void testUpdateEndpoint() throws Exception {
        when(monitoredEndpointService.updateEndpoint(eq(1L), any(MonitoredEndpointDto.class), any(User.class)))
                .thenReturn(testEndpoint);

        mockMvc.perform(put("/api/endpoints/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testEndpoint))
                        .requestAttr("user", testUser))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Endpoint"))
                .andExpect(jsonPath("$.url").value("https://test.com"));
    }

    @Test
    public void testDeleteEndpoint() throws Exception {
        mockMvc.perform(delete("/api/endpoints/1")
                        .requestAttr("user", testUser))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testGetMonitoringResults() throws Exception {
        when(monitoredEndpointService.getEndpointByIdForUser(eq(1L), any(User.class)))
                .thenReturn(testEndpoint);
        when(monitoringResultService.getLast10ResultsForEndpoint(any()))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/endpoints/1/results")
                        .requestAttr("user", testUser))
                .andExpect(status().isOk());
    }
} 