package dev.meirong.demos.webflux.trace;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Configuration
public class WebClientConfig {
    
    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder()
                .filter((request, next) -> {
                    // 从 Reactor Context 获取 TraceID
                    return Mono.deferContextual(ctx -> {
                        ClientRequest newRequest = ClientRequest.from(request)
                                .header("X-Trace-Id", ctx.get("traceId"))
                                .build();
                        return next.exchange(newRequest);
                    });
                });
    }
}