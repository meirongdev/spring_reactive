package dev.meirong.demos.webflux.reactor.operators;

import reactor.core.publisher.Flux;

// 使用Cache操作符缓存数据流中的元素，以便多次订阅时重用这些元素，而不是重新计算
public class CacheOperationExample {
    public static void main(String[] args) {
        Flux<Integer> numbers = Flux.just(1, 2, 3).cache();
        numbers.subscribe(System.out::println); // First subscriber
        numbers.subscribe(System.out::println); // Second subscriber (uses cached values)
    }
}
