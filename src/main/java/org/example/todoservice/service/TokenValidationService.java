package org.example.todoservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class TokenValidationService {

    private final WebClient webClient;

    public TokenValidationService(@Value("${user.service.url}") String userServiceUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(userServiceUrl)
                .build();
    }

    public boolean validateToken(String token) {
        try {
            String response = webClient.get()
                    .uri("/checkToken")
                    .header("Authorization", token)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            return response != null;
        } catch (Exception e) {
            return false;
        }
    }
}