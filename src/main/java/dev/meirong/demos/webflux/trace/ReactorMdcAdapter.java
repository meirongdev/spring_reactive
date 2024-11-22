package dev.meirong.demos.webflux.trace;

import org.slf4j.MDC;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ReactorMdcAdapter {
    // 为Mono添加TraceID上下文
    public static <T> Mono<T> withTraceId(Mono<T> mono) {
        return Mono.deferContextual(ctx -> {
            String traceId = ctx.getOrDefault("traceId", "unknown");
            return mono
                .doFirst(() -> MDC.put("traceId", traceId))
                .doFinally(signal -> MDC.clear());
        });
    }

    // 为Flux添加TraceID上下文
    public static <T> Flux<T> withTraceId(Flux<T> flux) {
        return Flux.deferContextual(ctx -> {
            String traceId = ctx.getOrDefault("traceId", "unknown");
            return flux
                .doFirst(() -> MDC.put("traceId", traceId))
                .doFinally(signal -> MDC.clear());
        });
    }
}