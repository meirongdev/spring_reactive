package dev.meirong.demos.webflux.reactor.operators;

import reactor.core.publisher.Mono;

public class SideEffectOperationsExample {
    public static void main(String[] args) {
        Mono.just(10)
                .doOnSubscribe(subscription -> System.out.println("Subscribed"))
                .doOnSuccess(value -> System.out.println("Success: " + value))
                .doOnError(error -> System.err.println("Error: " + error.getMessage()))
                .doOnTerminate(() -> System.out.println("Completed"))
                .subscribe();
    }
}