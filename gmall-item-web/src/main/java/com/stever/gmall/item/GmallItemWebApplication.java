package com.stever.gmall.item;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.util.ArrayList;

@SpringBootApplication
@ComponentScan(basePackages = "com.stever.gmall")
public class GmallItemWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(GmallItemWebApplication.class, args);
    }
}
