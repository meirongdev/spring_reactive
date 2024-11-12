package dev.meirong.demos.webflux.reactor.operators;

import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;

public class ZipOperationExample {
    public static void main(String[] args) {
        Flux<Integer> numbers1 = Flux.just(1, 2, 3);
        Flux<Integer> numbers2 = Flux.just(10, 20, 30);
        Flux<Tuple2<Integer, Integer>> zipped = Flux.zip(numbers1, numbers2);
        zipped.subscribe(tuple -> System.out.println(tuple.getT1() + ", " + tuple.getT2()));
    }
}