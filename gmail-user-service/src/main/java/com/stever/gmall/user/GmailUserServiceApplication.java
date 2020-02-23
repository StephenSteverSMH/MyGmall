package com.stever.gmall.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "com.stever.gmall.user.mapper")
public class GmailUserServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(GmailUserServiceApplication.class);
    }
}
