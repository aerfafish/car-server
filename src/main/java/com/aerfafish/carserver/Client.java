package com.aerfafish.carserver;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;

public class Client {

    private final static Log logger = LogFactory.getLog(Client.class);

    public static void main(String[] args) {

        try {
            SpringApplication.run(Client.class, args);
        } catch (Exception e) {
            logger.error("start agent failed", e);
            System.exit(1);
        }
    }
}
