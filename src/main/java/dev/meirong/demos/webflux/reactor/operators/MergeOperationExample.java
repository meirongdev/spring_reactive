package dev.meirong.demos.webflux.reactor.operators;

import reactor.core.publisher.Flux;

public class MergeOperationExample {
    public static void main(String[] args) {
        Flux<Integer> numbers1 = Flux.just(1, 2, 3);
        Flux<Integer> numbers2 = Flux.just(4, 5, 6);
        Flux<Integer> merged = Flux.merge(numbers1, numbers2);
        merged.subscribe(System.out::println);
    }
}