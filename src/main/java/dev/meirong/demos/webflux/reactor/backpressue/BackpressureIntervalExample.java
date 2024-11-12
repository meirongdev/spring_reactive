package dev.meirong.demos.webflux.reactor.backpressue;

import reactor.core.publisher.Flux;
import java.time.Duration;

public class BackpressureIntervalExample {
    public static void main(String[] args) {
        // We can apply backpressure to control the rate of emission.
        Flux<Long> intervalNumbers = Flux.interval(Duration.ofSeconds(1));
        intervalNumbers
                // We can control how many elements are requested at a time., default is the maximum value of Long.
                .doOnRequest(requested -> System.out.println("Requested: " + requested))
                .subscribe(
                        value -> {
                            System.out.println("Received: " + value);
                            try {
                                Thread.sleep(2000); // Simulate slower processing
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        },
                        error -> System.err.println("Error: " + error.getMessage()));
        // Sleep to observe backpressure in action
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}