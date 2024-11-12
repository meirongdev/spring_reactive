package dev.meirong.demos.webflux.reactor.operators;

import reactor.core.publisher.Flux;
import reactor.core.publisher.GroupedFlux;

public class GroupByOperationExample {
    public static void main(String[] args) {
        Flux<Integer> numbers = Flux.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        // 使用 groupBy 操作符将数据流分组, 通过 keySelector 函数将数据流分组为两个子流, 一个是偶数, 一个是奇数
        // GroupedFlux<String, Integer> 是一个包含 key 和数据流的对象
        Flux<GroupedFlux<String, Integer>> groupedByEvenOdd = numbers.groupBy(n -> n % 2 == 0 ? "Even" : "Odd");
        groupedByEvenOdd.subscribe(group -> {
            group.subscribe(number -> System.out.println(group.key() + ": " + number));
        });
    }
}