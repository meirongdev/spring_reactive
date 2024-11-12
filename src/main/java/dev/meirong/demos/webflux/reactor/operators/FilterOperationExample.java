package dev.meirong.demos.webflux.reactor.operators;

import reactor.core.publisher.Flux;

public class FilterOperationExample {
    public static void main(String[] args) {
        Flux<Integer> numbers = Flux.just(1, 2, 3, 4, 5);
        Flux<Integer> evenNumbers = numbers.filter(n -> n % 2 == 0);
        evenNumbers.subscribe(System.out::println);
    }
}