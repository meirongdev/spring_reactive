package dev.meirong.demos.webflux.reactor.operators;

import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import java.time.Duration;

public class RetryWhenOperationExample {
    public static void main(String[] args) {
        int maxRetries = 3;
        Mono<Integer> result = Mono.fromCallable(() -> {
            if (Math.random() < 0.6) {
                // 如果不输出异常信息，那么我们就无法知道是哪次重试失败了
                System.out.println("Random error occurred");
                throw new RuntimeException("Random error occurred");
            }
            return 42;
        })
                .retryWhen(Retry.fixedDelay(maxRetries, Duration.ofSeconds(1)));
        result.subscribe(
                value -> System.out.println("Result: " + value),
                error -> System.err.println("Error: " + error.getMessage()));

        // 延长主线程的生命周期，以便观察输出
        try {
            Thread.sleep(4000); // 让主线程等待10秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}