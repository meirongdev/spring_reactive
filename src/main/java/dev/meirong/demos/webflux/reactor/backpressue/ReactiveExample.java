package dev.meirong.demos.webflux.reactor.backpressue;

import java.util.concurrent.Flow.*;

// Implementing a simple Publisher and Subscriber to demonstrate backpressure
// The Publisher sends items to the Subscriber, and the Subscriber requests items from the Publisher
// The Publisher sends items to the Subscriber only when the Subscriber requests them
public class ReactiveExample {
    public static void main(String[] args) {
        Publisher<Integer> publisher = subscriber -> {
            
            subscriber.onSubscribe(new Subscription() {
                // Subscription的可以用于控制Publisher发送数据的速率
                private int counter = 0;
                private boolean completed = false;

                @Override
                public void request(long n) {
                    for (int i = 0; i < n; i++) {
                        if (!completed) {
                            subscriber.onNext(counter++);
                        } else {
                            subscriber.onComplete();
                            break;
                        }
                    }
                }

                @Override
                public void cancel() {
                    completed = true;
                }
            });
        };
        Subscriber<Integer> subscriber = new Subscriber<>() {
            private Subscription subscription;
            private int receivedItems = 0;
            @Override
            public void onSubscribe(Subscription subscription) {
                this.subscription = subscription;
                subscription.request(5); // Request initial items
            }

            @Override
            public void onNext(Integer item) {
                System.out.println("Received: " + item);
                receivedItems++;
                if (receivedItems >= 10) {
                    subscription.cancel(); // Stop subscription after receiving 10 items
                } else {
                    subscription.request(1); // Request more items
                }
            }

            @Override
            public void onError(Throwable throwable) {
                System.err.println("Error: " + throwable.getMessage());
            }

            @Override
            public void onComplete() {
                System.out.println("Processing completed");
            }
        };
        publisher.subscribe(subscriber);
    }
}