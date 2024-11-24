package dev.meirong.demos.webflux.helloworld;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class HelloWorldRouter {
    
    @Bean
    public RouterFunction<ServerResponse> route(HelloWorldHandler handler) {
        return RouterFunctions.route()
                .GET("/", handler::hello)
                .build();
    }
}
