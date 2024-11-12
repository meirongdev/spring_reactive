package dev.meirong.demos.webflux.reactor.operators;

import reactor.core.publisher.Flux;

public class FlatMapOperationExample {
    public static void main(String[] args) {
        Flux<Integer> numbers = Flux.just(1, 2, 3);
        // 使用 flatMap 操作符将每个数字映射为一个新的 Flux
        // 每个数字 n 被映射为包含 n^2 和 n^3 的 Flux
        Flux<Integer> squaredNumbers = numbers.flatMap(n -> Flux.just(n * n, n * n * n));
        squaredNumbers.subscribe(System.out::println);
    }
}
