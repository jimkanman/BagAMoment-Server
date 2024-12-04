package com.jkm.jimkanman;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class JimkanmanApplication {

    public static void main(String[] args) {
        SpringApplication.run(JimkanmanApplication.class, args);
    }

    @PostConstruct
    private void setTimeZone() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
        System.out.println("Default TimeZone set to: " + TimeZone.getDefault().getID());
    }
}
