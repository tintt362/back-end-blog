package com.trongtin.blog.identity.service;


import com.trongtin.blog.identity.dto.request.email.SendEmailRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final WebClient.Builder webClientBuilder;

    @Value("${spring.brevo.api-key}")
    private String brevoApiKey;

    @Value("${spring.brevo.base-url}")
    private String brevoBaseUrl;

    private static final String EMAIL_ENDPOINT = "/v3/smtp/email";

    public Mono<String> sendEmailUsingBrevo(SendEmailRequest emailRequest) {
        WebClient webClient = webClientBuilder.build();

        return webClient.post()
                .uri(brevoBaseUrl + EMAIL_ENDPOINT)
                .header("api-key", brevoApiKey)
                .bodyValue(emailRequest)
                .retrieve()
                .bodyToMono(String.class)
                .doOnSuccess(result -> log.info("Email sent successfully: {}", result))
                .doOnError(error -> log.error("Failed to send email", error));
    }
}