package com.aerfafish.carserver;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;


@SpringBootApplication
@Component
public class Client {

    private static final Logger log = LoggerFactory.getLogger(Client.class);

    public static void main(String[] args) {

        try {
            SpringApplication.run(Client.class, args);
        } catch (Exception e) {
            log.info("start agent failed", e);
            System.exit(1);
        }
    }
}
