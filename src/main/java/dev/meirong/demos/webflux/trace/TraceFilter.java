package dev.meirong.demos.webflux.trace;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import reactor.core.publisher.Mono;
import reactor.util.context.Context;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TraceFilter implements WebFilter {
    private final TraceIdGenerator traceIdGenerator;

    public TraceFilter(TraceIdGenerator traceIdGenerator) {
        this.traceIdGenerator = traceIdGenerator;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String traceId = exchange.getRequest().getHeaders().getFirst("X-Trace-Id");
        if (traceId == null) {
            traceId = traceIdGenerator.generate();
        }
        
        // 添加到响应头
        exchange.getResponse().getHeaders().add("X-Trace-Id", traceId);
        
        return chain.filter(exchange)
                .contextWrite(Context.of("traceId", traceId));
    }
}