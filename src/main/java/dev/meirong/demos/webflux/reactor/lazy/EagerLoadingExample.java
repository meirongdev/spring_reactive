package dev.meirong.demos.webflux.reactor.lazy;

import reactor.core.publisher.Mono;

public class EagerLoadingExample {
    public static void main(String[] args) {
        Mono<Integer> eagerMono = Mono.just(getData());
        System.out.println("Eager Mono created, data production triggered immediately");
        eagerMono.subscribe(value -> {
            System.out.println("Received value: " + value);
        });
    }

    private static int getData() {
        System.out.println("Data production triggered");
        return 42;
    }
}
