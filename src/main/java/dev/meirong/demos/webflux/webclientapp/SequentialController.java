package dev.meirong.demos.webflux.webclientapp;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/sequential")
public class SequentialController {
    private final WebClient webClient;

    public SequentialController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.
        baseUrl("http://example.com/api").
        build();
    }

    // curl http://localhost:8080/sequential/data
    @GetMapping("/data")
    public Mono<String> getData() {
        // will return 404, can use `curl -v http://example.com/api/first` to check
        return webClient.get().uri("/first")
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> {
                    if (response.statusCode() == HttpStatus.NOT_FOUND) {
                        return Mono
                                .error(new ResourceNotFoundException("Resource not found at the specified endpoint"));
                    }
                    return Mono.error(new ClientException("Client error: " + response.statusCode()));
                })
                .bodyToMono(String.class)
                .flatMap(firstResponse -> webClient.get().uri("/second")
                        .retrieve()
                        .bodyToMono(String.class)
                        .map(secondResponse -> firstResponse + secondResponse));
    }

    
    class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String message) {
            super(message);
        }
    }

    class ClientException extends RuntimeException {
        public ClientException(String message) {
            super(message);
        }
    }
}