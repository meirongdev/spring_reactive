package dev.meirong.demos.webflux.parallelapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.ParallelFlux;
import reactor.core.scheduler.Schedulers;

@SpringBootApplication
public class ParallelApp {

    public static void main(String[] args) {
        SpringApplication.run(ParallelApp.class, args);
    }

    @Service
    public class ParallelService {
        public ParallelFlux<String> processParallel() {
            // Simulating a list of items to process
            Flux<String> items = Flux.just("Item 1", "Item 2", "Item 3", "Item 4", "Item 5");
            return items.parallel()
                    .runOn(Schedulers.parallel()) // Run each item on a parallel scheduler
                    .map(this::processItem); // Process each item in parallel
        }

        private String processItem(String item) {
            // Simulate processing time for each item
            try {
                Thread.sleep(1000); // Simulate 1-second processing time
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return "Processed: " + item + " ";
        }
    }

    @RestController
    public class ParallelController {
        @Autowired
        private ParallelService parallelService;
        // curl http://localhost:8080/process
        @GetMapping("/process")
        public ParallelFlux<String> processItems() {
            return parallelService.processParallel();
        }
    }
}
