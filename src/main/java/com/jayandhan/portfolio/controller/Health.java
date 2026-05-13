
package com.jayandhan.portfolio.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api")
public class Health {

    @GetMapping("/health")
    public String health(){
        return "OK";
    }

    @GetMapping("/health-check")
    public String healthCheck(){
        return "Server is ok and running";
    }
}