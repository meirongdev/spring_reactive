package dev.meirong.demos.webflux.optimization;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.blockhound.BlockHound;
import reactor.core.publisher.Flux;


@SpringBootApplication
public class OptimizationApp {

    static {
        // -XX:+AllowRedefinitionToAddDeleteMethods
        BlockHound.install();
    }
    public static void main(String[] args) {
        Flux<String> flux = Flux.just("foo", "bar")
                .map(value -> {
                    if ("bar".equals(value)) {
                        throw new RuntimeException("Error occurred");
                    }
                    return value;
                })
                .log()
                .checkpoint("Map operation failed")
                .onErrorReturn("fallback");
        flux.subscribe(System.out::println);
    }
}
