package dev.meirong.demos.webflux.trace;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class UserService {

    public Mono<String> processUserData(String userId) {
        return Mono.deferContextual(ctx -> {
            String traceId = ctx.get("traceId");
            log.info("[TraceID: {}] 开始处理用户数据: {}", traceId, userId);

            // 模拟一些处理逻辑
            return Mono.just(userId)
                    .map(id -> {
                        log.info("[TraceID: {}] 处理第一阶段: {}", traceId, id);
                        return "processed-" + id;
                    })
                    .flatMap(processedId -> {
                        log.info("[TraceID: {}] 处理第二阶段: {}", traceId, processedId);
                        return Mono.just(processedId + "-final");
                    });
        });
    }

    public Flux<String> processMultipleUsers(List<String> userIds) {
        return Flux.deferContextual(ctx -> {
            String traceId = ctx.get("traceId");
            log.info("[TraceID: {}] 开始处理批量用户数据: {}", traceId, userIds);

            // 模拟一些处理逻辑
            return Flux.fromIterable(userIds)
                    .map(id -> {
                        log.info("[TraceID: {}] 处理第一阶段: {}", traceId, id);
                        return "processed-" + id;
                    })
                    .flatMap(processedId -> {
                        log.info("[TraceID: {}] 处理第二阶段: {}", traceId, processedId);
                        return Mono.just(processedId + "-final");
                    });
        });
    }
}