package dev.meirong.demos.webflux;

import java.time.Duration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;

// In real-time systems like financial trading platforms or IoT devices, 
//   asynchronous programming is crucial for handling time-sensitive tasks and events. 
// For instance, an IoT device may asynchronously process sensor data 
//   and trigger actions based on real-time events, ensuring timely responses without blocking other operations.
@SpringBootApplication
public class RealTimeSystemWebFluxApplication {
    public static void main(String[] args) {
        SpringApplication.run(RealTimeSystemWebFluxApplication.class, args);
    }

    @RestController
    public static class RealTimeController {
        private final RealTimeService realTimeService;

        public RealTimeController(RealTimeService realTimeService) {
            this.realTimeService = realTimeService;
        }

        @GetMapping("/api/events")
        public Flux<String> getRealTimeEvents() {
            return realTimeService.generateEvents();
        }
    }

    @Service
    public static class RealTimeService {
        public Flux<String> generateEvents() {
            return Flux.interval(Duration.ofSeconds(1))
                    .map(index -> "Event " + index)
                    .take(10); // Limit to 10 events
        }
    }
}