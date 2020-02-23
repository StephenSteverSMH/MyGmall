package com.stever.gmall.cart;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.stever.gmall")
@MapperScan(basePackages = "com.stever.gmall.cart.mapper")
public class GmallCartServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(GmallCartServiceApplication.class);
    }
}
