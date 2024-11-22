package dev.meirong.demos.webflux.webclientapp;

import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;
import reactor.netty.http.HttpProtocol;
import reactor.netty.http.client.HttpClient;

@RestController
@RequestMapping("/http2")
public class Http2Controller {
    private final WebClient webClient;

    public Http2Controller(WebClient.Builder webClientBuilder) {
        HttpClient httpClient = HttpClient.create()
                .protocol(HttpProtocol.HTTP11, HttpProtocol.H2);
        this.webClient = webClientBuilder
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .baseUrl("https://www.google.com").build();
    }

    // curl http://localhost:8080/http2/data
    @GetMapping("/data")
    public Mono<String> getData() {
        return webClient.get()
                .uri("/")
                .retrieve()
                .bodyToMono(String.class);
    }
}