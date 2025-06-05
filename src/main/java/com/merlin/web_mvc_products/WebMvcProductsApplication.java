package com.merlin.web_mvc_products;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebMvcProductsApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebMvcProductsApplication.class, args);

        System.out.println("Application démarrée avec succès!");
        System.out.println("Accédez à http://localhost:8080 pour voir l'application");
    }

}
