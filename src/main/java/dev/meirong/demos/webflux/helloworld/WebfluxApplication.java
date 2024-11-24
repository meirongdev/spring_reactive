package dev.meirong.demos.webflux.helloworld;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class WebfluxApplication {

	public static void main(String[] args) {
		// SpringApplication.run(WebfluxApplication.class, args);
		ConfigurableApplicationContext context = SpringApplication.run(WebfluxApplication.class, args);
		HelloWorldClient client = context.getBean(HelloWorldClient.class);
		client.getMessage().subscribe(System.out::println);
	}

}
