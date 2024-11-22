package dev.meirong.demos.webflux.trace;

import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class TraceIdGenerator {
    public String generate() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
