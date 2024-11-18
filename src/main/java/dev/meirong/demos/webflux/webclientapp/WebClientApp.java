package dev.meirong.demos.webflux.webclientapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@SpringBootApplication
public class WebClientApp {

    public static void main(String[] args) {
        SpringApplication.run(WebClientApp.class, args);
    }

    @Configuration
    public class WebClientConfig {
        @Bean
        public WebClient webClient() {
            return WebClient.create("https://jsonplaceholder.typicode.com"); // Example API base URL
        }
    }

    @Service
    public class NetworkService {
        @Autowired
        private WebClient webClient;

        public Mono<String> fetchDataFromExternalAPI() {
            return webClient.get()
                    .uri("/posts/1") // Example API endpoint
                    .retrieve()
                    .bodyToMono(String.class);
        }
    }

    @RestController
    public class NetworkController {
        @Autowired
        private NetworkService networkService;

        //  curl http://localhost:8080/fetch-data 
        @GetMapping("/fetch-data")
        public Mono<String> fetchData() {
            return networkService.fetchDataFromExternalAPI();
        }
    }
}