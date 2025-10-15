package com.fundly;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class FundlyApplication {

    public static void main(String[] args) {
        SpringApplication.run(FundlyApplication.class, args);
    }

}
