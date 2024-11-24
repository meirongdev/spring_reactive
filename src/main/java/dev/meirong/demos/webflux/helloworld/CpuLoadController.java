package dev.meirong.demos.webflux.helloworld;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class CpuLoadController {

    @GetMapping("/cpu")
    public Mono<Long> calculateHeavyTask() {
        return Mono.fromSupplier(() -> fibonacci(40));  // 40 是个大数，模拟较高的计算负载
    }

    private long fibonacci(int n) {
        if (n <= 1) return n;
        return fibonacci(n - 1) + fibonacci(n - 2);
    }
}