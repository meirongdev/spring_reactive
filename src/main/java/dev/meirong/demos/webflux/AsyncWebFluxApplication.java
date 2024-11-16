package dev.meirong.demos.webflux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@SpringBootApplication
public class AsyncWebFluxApplication {
    public static void main(String[] args) {
        SpringApplication.run(AsyncWebFluxApplication.class, args);
    }

    @RestController
    public static class AsyncController {
        private final DataService dataService;

        public AsyncController(DataService dataService) {
            this.dataService = dataService;
        }

        @GetMapping("/api/data")
        public Mono<String> fetchData() {
            return dataService.fetchDataAsync();
        }
    }

    @Service
    public static class DataService {
        
        // public Mono<String> fetchDataAsync() {
        //     return Mono.delay(java.time.Duration.ofMillis(200))
        //             .map(aLong -> "Data fetched from the server");
        // }
        public Mono<String> fetchDataAsync() {
            return Mono.fromSupplier(() -> {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "Data fetched from the server";
            });
        }
    }
}