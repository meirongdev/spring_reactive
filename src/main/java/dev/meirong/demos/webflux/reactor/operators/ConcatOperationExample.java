package dev.meirong.demos.webflux.reactor.operators;

import reactor.core.publisher.Flux;

// The Concat operation concatenates multiple streams sequentially, ensuring that elements from each stream are emitted in order.d
public class ConcatOperationExample {
    public static void main(String[] args) {
        Flux<Integer> numbers1 = Flux.just(1, 2, 3);
        Flux<Integer> numbers2 = Flux.just(4, 5, 6);
        Flux<Integer> concatenated = Flux.concat(numbers1, numbers2);
        concatenated.subscribe(System.out::println);
    }
}