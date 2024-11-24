package dev.meirong.demos.webflux.debug;

import reactor.core.publisher.Flux;

public class DebugApp {
    public static void main(String[] args) {

        // 启用详细的调试信息
        System.setProperty("reactor.trace.operatorStacktrace", "true");

        Flux<String> flux = Flux.just("foo", "bar")
                .map(value -> {
                    if ("bar".equals(value)) {
                        throw new RuntimeException("Error occurred");
                    }
                    return value;
                })
                .log()
                // seems not working
                .checkpoint("==================Error at map operation")
                // .checkpoint("Map operation failed")
                .onErrorReturn("fallback");
        flux.subscribe(System.out::println);

    }
}
