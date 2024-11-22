package dev.meirong.demos.webflux.trace;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    // curl http://localhost:8080/user/1
    @GetMapping("/users/{userId}")
    public Mono<String> getUserProcessing(@PathVariable String userId) {
        return Mono.deferContextual(ctx -> {
            String traceId = ctx.get("traceId");
            log.info("[TraceID: {}] 接收到用户请求: {}", traceId, userId);
            
            return userService.processUserData(userId)
                .map(result -> {
                    log.info("[TraceID: {}] 请求处理完成: {}", traceId, result);
                    return result;
                });
        });
    }

    // curl -X POST -H "Content-Type: application/json" -d '["1", "2", "3"]' http://localhost:8080/users/batch
    @PostMapping("/users/batch")
    public Flux<String> batchUserProcessing(@RequestBody List<String> userIds) {
        return Flux.deferContextual(ctx -> {
            String traceId = ctx.get("traceId");
            log.info("[TraceID: {}] 接收到批量用户请求: {}", traceId, userIds);
            
            return userService.processMultipleUsers(userIds)
                .doOnComplete(() -> log.info("[TraceID: {}] 批量请求处理完成", traceId));
        });
    }
}