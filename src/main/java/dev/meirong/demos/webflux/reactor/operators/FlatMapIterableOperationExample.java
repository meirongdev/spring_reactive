package dev.meirong.demos.webflux.reactor.operators;

import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

public class FlatMapIterableOperationExample {
    public static void main(String[] args) {
        Flux<String> words = Flux.just("Hello", "Reactor", "World");
        Flux<Character> letters = words.flatMapIterable(word -> {
            List<Character> characters = new ArrayList<>();
            for (char c : word.toCharArray()) {
                characters.add(c);
            }
            return characters;
        });
        letters.subscribe(System.out::println);
    }
}