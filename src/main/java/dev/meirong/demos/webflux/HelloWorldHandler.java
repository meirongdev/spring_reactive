package dev.meirong.demos.webflux;

import java.time.Duration;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

@Component

public class HelloWorldHandler {
    
    public Mono<ServerResponse> hello(ServerRequest request) {
        return Mono.delay(Duration.ofMillis(500))
                .flatMap(aLong -> ServerResponse.ok()
                        .contentType(MediaType.TEXT_PLAIN)
                        .body(BodyInserters.fromValue("Hello, Spring WebFlux!")));
    }
}
