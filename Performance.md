# Webflux

## Preparation

- brew install hey

##  Direct delay to simulate task cost

```java
    public Mono<ServerResponse> hello(ServerRequest request) {
        return Mono.delay(Duration.ofMillis(500))
                .flatMap(aLong -> ServerResponse.ok()
                        .contentType(MediaType.TEXT_PLAIN)
                        .body(BodyInserters.fromValue("Hello, Spring WebFlux!")));
    }
```

```bash
hey -n 1000 -c 20 http://localhost:8080                                                                                   [5:28:39]

Summary:
  Total:        25.1797 secs
  Slowest:      0.5194 secs
  Fastest:      0.5008 secs
  Average:      0.5036 secs
  Requests/sec: 39.7145
  
  Total data:   22000 bytes
  Size/request: 22 bytes

Response time histogram:
  0.501 [1]     |
  0.503 [283]   |■■■■■■■■■■■■■■■■■■■■
  0.505 [579]   |■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
  0.506 [101]   |■■■■■■■
  0.508 [16]    |■
  0.510 [0]     |
  0.512 [3]     |
  0.514 [0]     |
  0.516 [4]     |
  0.518 [4]     |
  0.519 [9]     |■


Latency distribution:
  10% in 0.5021 secs
  25% in 0.5026 secs
  50% in 0.5032 secs
  75% in 0.5040 secs
  90% in 0.5048 secs
  95% in 0.5057 secs
  99% in 0.5173 secs

Details (average, fastest, slowest):
  DNS+dialup:   0.0000 secs, 0.5008 secs, 0.5194 secs
  DNS-lookup:   0.0000 secs, 0.0000 secs, 0.0015 secs
  req write:    0.0000 secs, 0.0000 secs, 0.0014 secs
  resp wait:    0.5034 secs, 0.5007 secs, 0.5168 secs
  resp read:    0.0001 secs, 0.0000 secs, 0.0033 secs

Status code distribution:
  [200] 1000 responses
```

```bash
 hey -n 10000 -c 10000 http://localhost:8080                                                                               [6:21:58]

Summary:
  Total:        1.0683 secs
  Slowest:      1.0517 secs
  Fastest:      0.5529 secs
  Average:      0.8904 secs
  Requests/sec: 9361.0120
  
  Total data:   220000 bytes
  Size/request: 22 bytes

Response time histogram:
  0.553 [1]     |
  0.603 [177]   |■■
  0.653 [551]   |■■■■■■■
  0.703 [700]   |■■■■■■■■■
  0.752 [1904]  |■■■■■■■■■■■■■■■■■■■■■■■■
  0.802 [0]     |
  0.852 [1]     |
  0.902 [275]   |■■■
  0.952 [524]   |■■■■■■■
  1.002 [2692]  |■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
  1.052 [3175]  |■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■


Latency distribution:
  10% in 0.6687 secs
  25% in 0.7159 secs
  50% in 0.9759 secs
  75% in 1.0129 secs
  90% in 1.0227 secs
  95% in 1.0279 secs
  99% in 1.0396 secs

Details (average, fastest, slowest):
  DNS+dialup:   0.1371 secs, 0.5529 secs, 1.0517 secs
  DNS-lookup:   0.0687 secs, 0.0000 secs, 0.2451 secs
  req write:    0.0075 secs, 0.0000 secs, 0.2472 secs
  resp wait:    0.5131 secs, 0.5001 secs, 0.5763 secs
  resp read:    0.0018 secs, 0.0000 secs, 0.0230 secs

Status code distribution:
  [200] 10000 responses
```

## Simulate blocking operation with `Thread.sleep(200)`

AsyncWebFluxApplication.java
```java
    @Service
    public static class DataService {
        
        // public Mono<String> fetchDataAsync() {
        //     return Mono.delay(java.time.Duration.ofMillis(200))
        //             .map(aLong -> "Data fetched from the server");
        // }
        public Mono<String> fetchDataAsync() {
            return Mono.fromSupplier(() -> {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "Data fetched from the server";
            });
        }
    }
```

```bash
hey -n 1000 -c 200 http://localhost:8080/api/data            [21:51:44]
^[[A
Summary:
  Total:        13.4965 secs
  Slowest:      3.8045 secs
  Fastest:      0.2737 secs
  Average:      2.3276 secs
  Requests/sec: 74.0932
  
  Total data:   25000 bytes
  Size/request: 25 bytes

Response time histogram:
  0.274 [1]     |
  0.627 [38]    |■■
  0.980 [15]    |■
  1.333 [18]    |■
  1.686 [22]    |■
  2.039 [62]    |■■■■
  2.392 [100]   |■■■■■■
  2.745 [702]   |■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
  3.098 [29]    |■■
  3.451 [11]    |■
  3.805 [2]     |


Latency distribution:
  10% in 1.6881 secs
  25% in 2.3892 secs
  50% in 2.4240 secs
  75% in 2.6239 secs
  90% in 2.6301 secs
  95% in 2.7050 secs
  99% in 3.1990 secs

Details (average, fastest, slowest):
  DNS+dialup:   0.0037 secs, 0.2737 secs, 3.8045 secs
  DNS-lookup:   0.0004 secs, 0.0000 secs, 0.0273 secs
  req write:    0.0005 secs, 0.0000 secs, 0.0253 secs
  resp wait:    2.3221 secs, 0.2475 secs, 3.7765 secs
  resp read:    0.0001 secs, 0.0000 secs, 0.0014 secs

Status code distribution:
  [200] 1000 responses
```

Replacing `sleep.time(200)` with `Mono.delay(Duration.ofMillis(200))` to mock asynchronous operation.

```java
    @Service
    public static class DataService {
        public Mono<String> fetchDataAsync() {
            return Mono.delay(java.time.Duration.ofMillis(200))
                    .map(aLong -> "Data fetched from the server");
        }
    }
```
It will be much faster.

```bash
hey -n 1000 -c 200 http://localhost:8080/api/data                                                                                                                                                                        [22:14:14]

Summary:
  Total:        1.4270 secs
  Slowest:      0.4686 secs
  Fastest:      0.2011 secs
  Average:      0.2705 secs
  Requests/sec: 700.7614
  
  Total data:   28000 bytes
  Size/request: 28 bytes

Response time histogram:
  0.201 [1]     |
  0.228 [489]   |■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
  0.255 [208]   |■■■■■■■■■■■■■■■■■
  0.281 [78]    |■■■■■■
  0.308 [18]    |■
  0.335 [6]     |
  0.362 [0]     |
  0.388 [0]     |
  0.415 [0]     |
  0.442 [115]   |■■■■■■■■■
  0.469 [85]    |■■■■■■■


Latency distribution:
  10% in 0.2053 secs
  25% in 0.2104 secs
  50% in 0.2288 secs
  75% in 0.2695 secs
  90% in 0.4414 secs
  95% in 0.4463 secs
  99% in 0.4588 secs

Details (average, fastest, slowest):
  DNS+dialup:   0.0037 secs, 0.2011 secs, 0.4686 secs
  DNS-lookup:   0.0003 secs, 0.0000 secs, 0.0239 secs
  req write:    0.0001 secs, 0.0000 secs, 0.0065 secs
  resp wait:    0.2650 secs, 0.2008 secs, 0.4434 secs
  resp read:    0.0004 secs, 0.0000 secs, 0.0112 secs

Status code distribution:
  [200] 1000 responses
```