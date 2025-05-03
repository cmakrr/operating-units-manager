package com.rnpc.operatingunit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class OperatingUnitApplication {
    public static void main(String[] args) {
        SpringApplication.run(OperatingUnitApplication.class, args);
    }

}
