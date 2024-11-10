package dev.meirong.demos.webflux.reactor.lazy;

import reactor.core.publisher.Mono;

public class LazyLoadingExample {
    public static void main(String[] args) {
        // Mono.defer()方法接受一个Supplier，Supplier返回一个Mono, 但是Supplier只有在订阅时才会被调用
        Mono<Integer> lazyMono = Mono.defer(() -> {
            System.out.println("Data production triggered");
            return Mono.just(42);
        });
        
        System.out.println("Lazy Mono created, but data production not triggered yet");
        lazyMono.subscribe(value -> {
            System.out.println("Received value: " + value);
        });
    }
}

