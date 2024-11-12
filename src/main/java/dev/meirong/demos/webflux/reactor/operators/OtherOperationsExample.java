package dev.meirong.demos.webflux.reactor.operators;

import java.time.Duration;

// Limit, Delay, and Publish Operations
import reactor.core.publisher.Flux;

public class OtherOperationsExample {
    public static void main(String[] args) {
        Flux<Integer> numbers = Flux.range(1, 10)
                .take(5) // Limit to 5 elements
                .delayElements(Duration.ofMillis(500)) // Delay each element by 500 milliseconds
                .publish()
                .autoConnect(2); // Share the subscription among 2 subscribers
        numbers.subscribe(System.out::println);
        numbers.subscribe(System.out::println);


        // 延长主线程的生命周期，以便观察输出
        try {
            Thread.sleep(10000); // 让主线程等待10秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}