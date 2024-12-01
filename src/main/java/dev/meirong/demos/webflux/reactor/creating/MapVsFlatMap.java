package dev.meirong.demos.webflux.reactor.creating;

import dev.meirong.demos.webflux.reactor.creating.MonoDemos.Student;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class MapVsFlatMap {
    
    public static void main(String[] args) {
        Mono<Student> mono = Mono.just(new Student("Jack", 90.0));

        Mono<String> mapMono = mono.map(student -> student.getName());
        Mono<String> flatMapMono = mono.flatMap(student -> Mono.just(student.getName()));
        log.info("mapMono: {}", mapMono.block());
        log.info("flatMapMono: {}", flatMapMono.block());
    }
}
