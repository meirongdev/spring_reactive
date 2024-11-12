package dev.meirong.demos.webflux.reactor.operators;

import reactor.core.publisher.Flux;

public class MapOperationExample {
    public static void main(String[] args) {
        Flux<Integer> numbers = Flux.just(1, 2, 3, 4, 5);
        Flux<Integer> squaredNumbers = numbers.map(n -> n * n);
        squaredNumbers.subscribe(System.out::println);
    }
}