package dev.meirong.demos.webflux.eventdrivenapp;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@SpringBootApplication
public class EventDrivenApp {

    public static void main(String[] args) {
        SpringApplication.run(EventDrivenApp.class, args);
    }


    @Component
    public static class EventPublisher {
        private final Sinks.Many<String> eventSink;
        private Disposable intervalSubscription;

        public EventPublisher() {
            this.eventSink = Sinks.many().multicast().onBackpressureBuffer();
        }

        @PostConstruct
        public void init() {
            intervalSubscription = Flux.interval(Duration.ofSeconds(1))
                    .map(sequence -> "Event " + sequence +" ")
                    .doOnNext(event -> {
                        Sinks.EmitResult result = eventSink.tryEmitNext(event);
                        if (result.isFailure()) {
                            // Log the failure or take appropriate action
                            System.err.println("Emission failed: " + result);
                        }
                    })
                    .subscribe();
        }

        public Flux<String> getEventStream() {
            return eventSink.asFlux();
        }

        @PreDestroy
        public void cleanup() {
            if (intervalSubscription != null && !intervalSubscription.isDisposed()) {
                intervalSubscription.dispose();
            }
        }
    }

    @RestController
    public class EventDrivenController {
        @Autowired
        private EventPublisher eventPublisher;

        @GetMapping("/events")
        public Flux<String> getEventStream() {
            return eventPublisher.getEventStream();
        }
    }
}
