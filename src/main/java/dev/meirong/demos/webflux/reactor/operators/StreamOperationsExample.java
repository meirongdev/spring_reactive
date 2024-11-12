package dev.meirong.demos.webflux.reactor.operators;

import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;

public class StreamOperationsExample {
    public static void main(String[] args) {
        Flux<Integer> numbers = Flux.just(1, 2, 3, 4, 5);
        // Map operation to double each number
        Flux<Integer> doubledNumbers = numbers.map(n -> n * 2);
        // Filter operation to select even numbers
        Flux<Integer> evenNumbers = numbers.filter(n -> n % 2 == 0);
        // FlatMap operation to convert each number to a stream of its digits
        Flux<Integer> digits = numbers.flatMap(n -> Flux.just(n % 10, n / 10));
        // Zip operation to combine numbers with their squares
        Flux<Tuple2<Integer, Integer>> zipped = numbers.zipWith(numbers.map(n -> n * n));
        // Merge operation to combine two streams
        Flux<Integer> merged = Flux.merge(doubledNumbers, evenNumbers);
        // Subscribe to and print the results
        doubledNumbers.subscribe(System.out::println);
        evenNumbers.subscribe(System.out::println);
        digits.subscribe(System.out::println);
        zipped.subscribe(tuple -> System.out.println(tuple.getT1() + " -> " + tuple.getT2()));
        merged.subscribe(System.out::println);
    }
}