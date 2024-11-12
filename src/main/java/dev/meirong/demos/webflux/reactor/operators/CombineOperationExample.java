package dev.meirong.demos.webflux.reactor.operators;

import reactor.core.publisher.Flux;

public class CombineOperationExample {
    // The Combine operation combines elements from multiple streams using a combinator function, producing a new stream of combined values.
    public static void main(String[] args) {
        Flux<Integer> numbers1 = Flux.just(1, 2, 3);
        Flux<Integer> numbers2 = Flux.just(10, 20, 30);
        // 使用 combineLatest 操作符将第一个数据流的最新元素和第二个数据流的元素逐个进行相加？
        Flux<Integer> combined = Flux.combineLatest(numbers1, numbers2, (n1, n2) -> n1 + n2);
        combined.subscribe(System.out::println);
    }
}