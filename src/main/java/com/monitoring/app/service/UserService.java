package com.monitoring.app.service;

import com.monitoring.app.model.User;
import com.monitoring.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * Finds a user by their access token
     *
     * @param accessToken the access token
     * @return the user if found
     */
    public Optional<User> findByAccessToken(String accessToken) {
        return userRepository.findByAccessToken(accessToken);
    }

    /**
     * Initialize predefined users
     */
    @PostConstruct
    public void init() {
        // Only seed if no users exist
        if (userRepository.count() == 0) {
            User applifting = new User();
            applifting.setUsername("Applifting");
            applifting.setEmail("info@applifting.cz");
            applifting.setAccessToken("93f39e2f-80de-4033-99ee-249d92736a25");

            User batman = new User();
            batman.setUsername("Batman");
            batman.setEmail("batman@example.com");
            batman.setAccessToken("dcb20f8a-5657-4f1b-9f7f-ce65739b359e");

            userRepository.saveAll(Arrays.asList(applifting, batman));
        }
    }
} 