package dev.meirong.demos.webflux.mysqlapp;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/users")
public class UserController {
    
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // curl -X POST -H "Content-Type: application/json" -d '{"name":"Meirong", "age": 25}' http://localhost:8080/users
    @PostMapping
    public Mono<User> createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    // curl http://localhost:8080/users/1
    @GetMapping("/{id}")
    public Mono<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    // curl http://localhost:8080/users
    @GetMapping
    public Flux<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // curl http://localhost:8080/users/name/Meirong
    @GetMapping("/name/{name}")
    public Mono<User> getUserByName(@PathVariable String name) {
        return userService.getUserByName(name);
    }
}