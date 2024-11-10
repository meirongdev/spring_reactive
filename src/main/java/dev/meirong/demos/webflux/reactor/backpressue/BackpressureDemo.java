package dev.meirong.demos.webflux.reactor.backpressue;

import org.reactivestreams.Subscription;

import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

public class BackpressureDemo {

    // Backpressure by request 10 items at a time
    public void subscriptionRequest() {
        Flux<Integer> rangeFlux = Flux.range(1, 100);
        rangeFlux.subscribe(new BaseSubscriber<Integer>() {
            @Override
            protected void hookOnSubscribe(Subscription subscription) {
                // Request 10 items at a time
                subscription.request(10);
            }

            @Override
            protected void hookOnNext(Integer value) {
                System.out.println(value);
            }

            @Override
            protected void hookOnComplete() {
                System.out.println("Flux Completed !!!");
            }

            @Override
            protected void hookOnError(Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }

    public static void main(String[] args) {
        BackpressureDemo demo = new BackpressureDemo();
        demo.subscriptionRequest();
    }
}
