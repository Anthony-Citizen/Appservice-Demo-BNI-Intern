package com.example.appservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@ComponentScan({"com.example.*"})
@EnableJpaRepositories("com.example.*")
@SpringBootApplication
public class AppserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppserviceApplication.class, args);
    }
}
