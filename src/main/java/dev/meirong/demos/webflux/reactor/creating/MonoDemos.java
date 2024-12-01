package dev.meirong.demos.webflux.reactor.creating;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class MonoDemos {

    // 以下的block()方法是阻塞操作，会阻塞当前线程，生产环境中不应该使用
    public static void main(String[] args) {
        // topic 1: Create a Mono

        Student student = new Student("Jack", 90.0);
        Mono<Student> mono = Mono.just(student);
        // Note: block() is a blocking operation, will block current thread, should not
        // be used in production code
        Student result = mono.block();
        log.info("result: {}", result);

        // Mono<Student> mono2 = Mono.just(null);
        // Student result2 = mono2.block();
        // log.info("result2: {}", result2); // Will throw NullPointerException

        // How to avoid NullPointerException

        // 1. Use Mono.justOrEmpty
        Mono<Student> mono3 = Mono.justOrEmpty(null);
        Student result3 = mono3.block();
        log.info("result3: {}", result3); // Will not throw NullPointerException, but result3 is null

        // 2. Use Mono.blockOptional
        Mono<Student> mono4 = Mono.justOrEmpty(null);
        Optional<Student> optional = mono4.blockOptional();
        optional.ifPresentOrElse(
                student4 -> log.info("student4: {}", student4),
                () -> log.info("student4 is null"));
        log.info("optional: {}", optional); // Optional.empty

        // 3. Use Mono.empty
        Mono<Student> mono5 = Mono.empty();
        Student result5 = mono5.block();
        log.info("result5: {}", result5); // null

        // topic 2: Create a Mono with error
        // Mono that terminates with an error immediately after being subscribed to.

        Mono<String> failMono = Mono.error(new NoSuchFieldException());
        try{
            String name = failMono.block();
            log.info("name: {}", name); // Will throw NoSuchFieldException
        } catch (Exception e) {
            log.error("failed Mono error: {}", e);
        }

        // Solution: Use onErrorReturn, onErrorResume, or onErrorContinue, similar to Optional.orElse, Optional.orElseGet, Optional.orElseThrow
        Mono<String> failMono2 = Mono.<String>error(new NoSuchFieldException())
                .onErrorReturn("Unknown");
        // use subscribe to handle the mono asynchronously
        failMono2.subscribe(name -> log.info("name2 with subscribe: {}", name), error -> log.error("error: {}", error));

        String name2 = failMono2.block();
        log.info("name2: {}", name2); // Unknown
        // Failed Mono并不会返回一个value，而是返回一个error，所以block()方法会抛出异常，但是onErrorReturn()方法会返回一个默认值
        
    }

    @Data
    @AllArgsConstructor
    static class Student {
        private String name;
        private double score;
    }
}
