package dev.meirong.demos.webflux.reactor.operators;

import reactor.core.publisher.Flux;

public class ExpandOperationExample {
    public static void main(String[] args) {
        Flux<Integer> numbers = Flux.just(1, 2, 3);
        // 使用 expand 操作符对每个元素进行扩展
        // 对每个元素 n，生成一个新的 Flux，其中包含 n * 2，并且只包含小于 10 的元素
        Flux<Integer> expanded = numbers.expand(n -> Flux.just(n * 2).takeWhile(x -> x < 10));
        expanded.subscribe(System.out::println);
    }
}