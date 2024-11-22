package dev.meirong.demos.webflux.webclientapp;

import java.time.Duration;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/cache")
public class CacheController {
    private final WebClient webClient;
    private final Cache<String, Mono<String>> responseCache;

    public CacheController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://jsonplaceholder.typicode.com").build();
        this.responseCache = Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofMinutes(5))
                .maximumSize(100)
                .build();
    }

    // curl -i http://localhost:8080/cache/data
    @GetMapping("/data")
    public Mono<String> getData() {
        return responseCache.get("data", key -> webClient.get()
                .uri("/")
                .retrieve()
                .bodyToMono(String.class));
    }
}