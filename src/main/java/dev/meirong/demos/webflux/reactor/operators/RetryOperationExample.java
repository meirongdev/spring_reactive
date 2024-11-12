package dev.meirong.demos.webflux.reactor.operators;

import reactor.core.publisher.Mono;

public class RetryOperationExample {
    static int count = 1;

    public static void main(String[] args) {
        int maxRetries = 3;
        // Retry the operation up to a maximum number of times
        Mono<Integer> result = Mono.fromCallable(() -> {
            System.out.println("Retry count " + count);
            count++;
            if (Math.random() < 0.5) {
                throw new RuntimeException("Random error occurred");
            }
            return 42;
        }).retry(maxRetries);
        
        result.subscribe(
                value -> System.out.println("Result: " + value),
                error -> System.err.println("Error: " + error.getMessage()));
    }
}