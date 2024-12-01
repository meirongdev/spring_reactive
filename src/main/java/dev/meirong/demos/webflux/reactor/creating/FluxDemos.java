package dev.meirong.demos.webflux.reactor.creating;

import java.util.List;
import java.util.stream.Stream;

import dev.meirong.demos.webflux.reactor.creating.MonoDemos.Student;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
public class FluxDemos {
    
    public static void main(String[] args) {
        // 1. from iterable
        List<Student> students = List.of(
            new Student("Jack", 90.0), 
            new Student("Tom", 80.0));
        
        Flux<Student> flux = Flux.fromIterable(students);
        // Flux abstracts not an iterable, but a sequence of elements
        // - blockFirst() will return the first element in the sequence
        // - blockLast() will return the last element in the sequence

        Student first = flux.blockFirst();
        Student last = flux.blockLast();
        log.info("first: {}", first);
        log.info("last: {}", last);

        // elementAt will return the element at the specified index
        Student student = flux.elementAt(1).block();
        log.info("student: {}", student);
    
        // 2. from Streams
        Stream<Student> studentStream = students.stream();
        Flux<Student> flux2 = Flux.fromStream(studentStream);
        // Student first2 = flux2.blockFirst();
        // log.info("first2: {}", first2);
        Iterable<Student> studentIterable = flux2.toIterable();
        studentIterable.forEach(s -> log.info("s: {}", s));

        // 我们虽然可以通过 Flux.fromStream() 方法将 Stream 转换为 Flux，但是这种方式并不推荐，因为 Stream 是一次性的，而 Flux 是可以多次订阅的。
        // 如果我们将 Stream 转换为 Flux，那么第一个订阅者会消费 Stream 中的所有元素，第二个订阅者将无法消费任何元素。
        // 如果我们需要多次订阅，应该使用 Flux.defer() 方法，每次订阅都会创建一个新的 Stream。
        
        // 推荐使用 Mono和Flux封装Java async操作，切忌使用阻塞操作
    }
}
