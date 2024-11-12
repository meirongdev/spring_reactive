package dev.meirong.demos.webflux.reactor.backpressue;

import reactor.core.publisher.Flux;

public class SlowConsumerExample {
    public static void main(String[] args) {
        Flux<Integer> numbers = Flux.range(1, 100);
        numbers
                .doOnRequest(requested -> System.out.println("Requested: " + requested))
                .subscribe(
                        value -> {
                            System.out.println("Received: " + value);
                            try {
                                Thread.sleep(500); // Simulate slower processing
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        },
                        error -> System.err.println("Error: " + error.getMessage()),
                        () -> System.out.println("Completed"));
        // Sleep to observe backpressure in action
        try {
            Thread.sleep(30000); // Consumer requests slowly over 30 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}