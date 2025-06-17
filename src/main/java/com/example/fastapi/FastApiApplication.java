package com.example.fastapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FastApiApplication {

    public static void main(String[] args) {

        SpringApplication.run(FastApiApplication.class, args);
        System.out.println("✅ Server started successfully!");
    }

}
