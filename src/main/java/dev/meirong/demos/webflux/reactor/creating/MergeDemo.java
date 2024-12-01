package dev.meirong.demos.webflux.reactor.creating;

import java.time.Duration;

import reactor.core.publisher.Flux;

public class MergeDemo {

    public static void main(String[] args) {
        Flux<Integer> flux1 = Flux.just(1, 2, 3);
        Flux<Integer> flux2 = Flux.just(4, 5, 6);
        Flux<Integer> concatFlux = Flux.concat(
            flux1.delayElements(Duration.ofMillis(500)),
            flux2.delayElements(Duration.ofMillis(300))
        );
        // It will output sequentially
        Iterable<Integer> iterable = concatFlux.toIterable();
        iterable.forEach(System.out::println);

        Flux<Integer> mergeFlux = Flux.merge(
            flux1.delayElements(Duration.ofMillis(500)),
            flux2.delayElements(Duration.ofMillis(300))
        );
        // It will output in parallel
        iterable = mergeFlux.toIterable();
        iterable.forEach(System.out::println);
    }
}
