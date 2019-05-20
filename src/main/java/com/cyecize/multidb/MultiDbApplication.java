package com.cyecize.multidb;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class MultiDbApplication {

    public static ConfigurableApplicationContext springContext;

    @Value("${server.port}")
    private int port;

    public static void main(String[] args) {
        springContext = SpringApplication.run(MultiDbApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationLoaded() {
        System.out.println(String.format("http://localhost:%d", this.port));
    }
}
