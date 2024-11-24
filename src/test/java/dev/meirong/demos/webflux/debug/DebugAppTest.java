package dev.meirong.demos.webflux.debug;

import java.time.Duration;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class DebugAppTest {

    @Test
    public void test() {
        Flux<String> flux = Flux.just("foo", "bar", "baz");
        StepVerifier.create(flux)
                .expectNext("foo")
                .expectNext("bar")
                .expectNext("baz")
                .verifyComplete();
    }

    @Test
    public void testFluxWithError() {
        Flux<String> flux = Flux.just("Spring", "Reactor")
                .concatWith(Flux.error(new RuntimeException("Exception occurred")));
        StepVerifier.create(flux)
                .expectNext("Spring")
                .expectNext("Reactor")
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    public void testFluxWithDelay() {
        Flux<String> flux = Flux.just("Spring", "Reactor")
                .delayElements(Duration.ofMillis(100));
        StepVerifier.create(flux)
                .expectNext("Spring")
                .expectNext("Reactor")
                .verifyComplete();
    }
}
