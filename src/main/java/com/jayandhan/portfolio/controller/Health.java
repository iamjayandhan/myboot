
package com.jayandhan.portfolio.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.core.env.Environment;

@RestController
@RequestMapping("/api")
public class Health {

    private final Environment env;

    public Health(Environment env) {
        this.env = env;
    }

    @GetMapping("/health")
    public String health(){
        return "OK";
    }

    @GetMapping("/health-check")
    public String healthCheck(){
        return "Server is ok and running";
    }

    @GetMapping("/environment")
    public String environment() {
        String[] profiles = env.getActiveProfiles();
        if (profiles.length == 0) {
            profiles = env.getDefaultProfiles();
        }
        return "Current Environment: " + String.join(", ", profiles);
    }
}