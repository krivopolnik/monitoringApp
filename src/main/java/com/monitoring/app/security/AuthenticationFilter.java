package com.monitoring.app.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monitoring.app.dto.ErrorResponse;
import com.monitoring.app.model.User;
import com.monitoring.app.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.lang.NonNull;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

    private static final String ACCESS_TOKEN_HEADER = "X-Access-Token";
    
    private final UserService userService;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, 
                                   @NonNull HttpServletResponse response, 
                                   @NonNull FilterChain filterChain) 
            throws ServletException, IOException {
        
        // Skip authentication for OPTIONS requests (CORS)
        if (request.getMethod().equals("OPTIONS")) {
            filterChain.doFilter(request, response);
            return;
        }
        
        String accessToken = request.getHeader(ACCESS_TOKEN_HEADER);
        
        if (accessToken == null || accessToken.isEmpty()) {
            sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "Access token is required");
            return;
        }
        
        Optional<User> userOptional = userService.findByAccessToken(accessToken);
        
        if (userOptional.isPresent()) {
            // Store user in request attribute for future use
            request.setAttribute("user", userOptional.get());
            filterChain.doFilter(request, response);
        } else {
            sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "Invalid access token");
        }
    }

    private void sendErrorResponse(HttpServletResponse response, HttpStatus status, String message) 
            throws IOException {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        
        ErrorResponse errorResponse = new ErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                message,
                "/"
        );
        
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
    
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // Add paths that should not be filtered
        String path = request.getServletPath();
        return path.equals("/api/health") || path.startsWith("/api-docs") || path.startsWith("/swagger-ui");
    }
} 