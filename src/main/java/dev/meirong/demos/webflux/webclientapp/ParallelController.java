package dev.meirong.demos.webflux.webclientapp;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/parallel")
public class ParallelController {
    private final WebClient webClient;

    public ParallelController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://example.com/api").build();
    }
    // curl http://localhost:8080/parallel/data
    @GetMapping("/data")
    public Mono<String> getData() {
        Mono<String> firstCall = webClient.get().uri("/first").retrieve().bodyToMono(String.class);
        Mono<String> secondCall = webClient.get().uri("/second").retrieve().bodyToMono(String.class);
        return Mono.zip(firstCall, secondCall)
                .map(tuple -> tuple.getT1() + tuple.getT2());
    }
}