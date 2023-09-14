package com.example.demo;

import jakarta.servlet.ServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    public String index(ServletRequest request) {
        return "Greetings from Spring Boot using " + request.getProtocol() + "!";
    }

}
