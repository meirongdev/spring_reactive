package dev.meirong.demos.webflux.reactor.backpressure;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class BackpressureTest {

    @Test
    public void cancelCallback() {
        Flux<Integer> rangeFlux = Flux.range(1, 100).log();
        rangeFlux.doOnCancel(() -> {
            System.out.println("Cancelled !!!");
        }).doOnComplete(() -> {
            System.out.println("Completed !!!");
        }).subscribe(new BaseSubscriber<Integer>() {
            @Override
            protected void hookOnNext(Integer value) {
                try {
                    Thread.sleep(500);
                    request(1); // request next element
                    System.out.println(value);
                    if (value == 5) {
                        cancel();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        // Test the code
        StepVerifier.create(rangeFlux)
                .expectNext(1, 2, 3, 4, 5)
                .thenCancel()
                .verify();
    }

    @Test
    public void backpressureVerifier() {
        Flux<Integer> data$ = Flux.just(101, 201, 301).log();
        StepVerifier.create(data$)
                .expectSubscription()
                .thenRequest(1)
                .expectNext(101)
                .thenRequest(2)
                .expectNext(201, 301)
                .verifyComplete();
    }
}
