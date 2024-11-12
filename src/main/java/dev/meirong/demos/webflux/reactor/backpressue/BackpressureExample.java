package dev.meirong.demos.webflux.reactor.backpressue;

import reactor.core.publisher.Flux;

public class BackpressureExample {
    public static void main(String[] args) {
        Flux<Integer> numbers = Flux.range(1, 100);
        numbers
        // we can control how many elements are requested at a time.
                .doOnRequest(requested -> System.out.println("Requested: " + requested))
                .subscribe(
                        value -> {
                            System.out.println("Received: " + value);
                            try {
                                Thread.sleep(100); // Simulate processing delay
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        },
                        error -> System.err.println("Error: " + error.getMessage()),
                        () -> System.out.println("Completed"));
        // Sleep to observe backpressure in action
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
