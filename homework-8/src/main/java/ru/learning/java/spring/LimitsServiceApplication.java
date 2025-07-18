package ru.learning.java.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LimitsServiceApplication {
    public static void main(String[] args) {SpringApplication.run(LimitsServiceApplication.class, args);
    }
}