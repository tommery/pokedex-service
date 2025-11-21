package com.sita;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.sita")
public class PokedexServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(PokedexServiceApplication.class, args);
    }
}
