package com.example.appservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@ComponentScan({"com.example.*"})
@EnableJpaRepositories("com.example.*")
@SpringBootApplication
public class AppserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppserviceApplication.class, args);
    }
}
