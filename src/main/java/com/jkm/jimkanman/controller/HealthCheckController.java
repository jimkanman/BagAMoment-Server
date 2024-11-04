package com.jkm.jimkanman.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
public class HealthCheckController {
    @GetMapping("/health")
    public ResponseEntity<Object> amIAlive(){
        return new ResponseEntity<>("Hello!", HttpStatus.OK);
    }
}
