package com.doan.cinemaserver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;

@Slf4j
@SpringBootApplication
@EnableCaching
@EnableAsync
public class CinemaServerApplication {

    public static void main(String[] args) {
        Environment env = SpringApplication.run(CinemaServerApplication.class, args).getEnvironment();
        String appName = env.getProperty("spring.application.name");
        if (appName != null) {
            appName = appName.toUpperCase();
        }
        String port = env.getProperty("server.port");
        log.info("-------------------------START {} Application------------------------------", appName);
        log.info("   Application         : {}", appName);
        log.info("   Url swagger-ui      : http://localhost:{}/swagger-ui.html", port);
        log.info("-------------------------START SUCCESS {} Application----------------------", appName);
    }

}
