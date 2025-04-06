package com.hack.Spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling  // Включаем планировщик задач
public class Application {
  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
