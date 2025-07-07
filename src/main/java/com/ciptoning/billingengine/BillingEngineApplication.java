package com.ciptoning.billingengine;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BillingEngineApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(BillingEngineApplication.class, args);
    }

    // Chose a simple web app for easier testing and better feel during development.
    @Override
    public void run(String... args) {
        System.out.println("=== Application Started ===");
        System.out.println("Visit http://localhost:8080 to use the web interface.");
        System.out.println("===========================");
    }

}
