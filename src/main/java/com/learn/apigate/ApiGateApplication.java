package com.learn.apigate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableZuulProxy
public class ApiGateApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGateApplication.class, args);
    }

}
