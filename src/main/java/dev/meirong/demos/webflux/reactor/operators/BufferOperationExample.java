package dev.meirong.demos.webflux.reactor.operators;

import java.util.List;

import reactor.core.publisher.Flux;

// buffer 操作符会根据指定的条件对源 Flux 进行缓冲。
public class BufferOperationExample {
    public static void main(String[] args) {
        // buffer based on size
        // Flux<Integer> numbers = Flux.range(1, 10);
        // Flux<List<Integer>> buffered = numbers.buffer(3);
        // buffered.subscribe(System.out::println);

        // buffer based on time window
        // 这行代码创建了一个 Flux，它每秒发出一个元素，从 0 开始递增。一共sleep 10秒，所以会发出10个元素。
        Flux<Long> interval = Flux.interval(java.time.Duration.ofMillis(1000));
        // bufferTimeout 操作符会根据指定的时间窗口或元素数量进行缓冲。
        // 在这里，缓冲操作会在以下两种情况之一发生时发出缓冲的元素：
        // 缓冲的元素数量达到 5 个。
        // 缓冲的时间达到 2 秒。
        Flux<List<Long>> bufferedTime = interval.bufferTimeout(5, java.time.Duration.ofMillis(2000));
        bufferedTime.subscribe(System.out::println);

        // 延长主线程的生命周期，以便观察输出
        try {
            Thread.sleep(10000); // 让主线程等待10秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}