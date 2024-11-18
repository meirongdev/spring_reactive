package dev.meirong.demos.webflux.resilience4j;

import java.time.Duration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.reactor.circuitbreaker.operator.CircuitBreakerOperator;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class Resilience4jApp {

    public static void main(String[] args) {
        SpringApplication.run(Resilience4jApp.class, args);
    }

    @Configuration
    public class WebClientConfig {
        @Bean
        public WebClient webClient() {
            return WebClient.builder()
                    .baseUrl("http://example.com/api")
                    .filter(ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
                        System.out.println("Request: " + clientRequest.method() + " " + clientRequest.url());
                        return Mono.just(clientRequest);
                    }))
                    .filter(ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
                        System.out.println("Response: " + clientResponse.statusCode());
                        return Mono.just(clientResponse);
                    }))
                    .build();
        }

        @Bean
        public CircuitBreakerRegistry circuitBreakerRegistry() {
            CircuitBreakerConfig config = CircuitBreakerConfig.custom()
                    .failureRateThreshold(50)
                    .waitDurationInOpenState(Duration.ofMillis(10000))
                    .slidingWindowSize(10)
                    .build();
            return CircuitBreakerRegistry.of(config);
        }
    }

    @RestController
    @RequestMapping("/resilience")
    public class Resilience4jController {
        private final WebClient webClient;
        private final CircuitBreaker circuitBreaker;


        public Resilience4jController(WebClient webClient, CircuitBreakerRegistry circuitBreakerRegistry) {
            this.webClient = webClient;
            this.circuitBreaker = circuitBreakerRegistry.circuitBreaker("webClientCircuitBreaker");
        }

        // curl http://localhost:8080/resilience/data
        // hey -n 100 -c 2 http://localhost:8080/resilience/data
        @GetMapping("/data")
        public Mono<String> fetchDataWithTimeoutAndRetry() {
            return webClient.get()
                    .uri("/data")
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(Duration.ofSeconds(3)) // Timeout after 3 seconds
                    .retry(2) // Retry twice before failing
                    .transformDeferred(CircuitBreakerOperator.of(circuitBreaker))
                    ; 
        }
    }
}
