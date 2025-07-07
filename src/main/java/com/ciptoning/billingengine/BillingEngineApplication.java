package com.ciptoning.billingengine;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BillingEngineApplication implements CommandLineRunner {

    private final BillingRepository repository;

    public BillingEngineApplication(BillingRepository repository) {
        this.repository = repository;
    }

    public static void main(String[] args) {
        SpringApplication.run(BillingEngineApplication.class, args);
    }

    @Override
    public void run(String... args) {
        System.out.println("=== Application Started ===");
        System.out.println("Visit http://localhost:8080 to use the web interface.");
        System.out.println("Existing users:");
        repository.findAll().forEach(user ->
                System.out.println(" - " + user.getUsername())
        );
        System.out.println("===========================");
    }

}
