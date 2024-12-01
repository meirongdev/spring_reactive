package dev.meirong.demos.webflux.reactor.creating;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class WrapAsyncOpWithMono {
    
    public static void main(String[] args) {
        // 1. 异步操作包装成 Mono
        Mono.fromCallable(() -> {
            // 异步操作
            Thread.sleep(1000);
            return "Hello, Reactor!";
        }).subscribe(System.out::println);
        
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
        .GET()
        .uri(URI.create
        ("https://jsonplaceholder.typicode.com/todos/1"))
        .build();
        CompletableFuture<String> response = client
        .sendAsync(request,
        HttpResponse.BodyHandlers.ofString())
        .thenApplyAsync(result -> result.body());
        
        Mono<String> mon = Mono.fromFuture(response);
        String result = mon.block();
        System.out.println(result);
    }
}
