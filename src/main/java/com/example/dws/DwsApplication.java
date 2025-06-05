package com.example.dws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication
@EnableSpringDataWebSupport
public class DwsApplication {

    public static void main(String[] args) {
        SpringApplication.run(DwsApplication.class, args);
    }

}
