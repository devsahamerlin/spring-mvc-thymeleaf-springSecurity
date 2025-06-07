package com.merlin.web_mvc_products;

import com.merlin.web_mvc_products.entities.Product;
import com.merlin.web_mvc_products.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.UUID;

//@SpringBootApplication
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class WebMvcProductsApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebMvcProductsApplication.class, args);

        System.out.println("Application démarrée avec succès!");
        System.out.println("Accédez à http://localhost:8080 pour voir l'application");
    }

    @Bean
    public CommandLineRunner start(
            ProductRepository productRepository) {
        return args -> {
            productRepository.save(Product.builder()
                    .id(UUID.randomUUID().toString())
                    .name("HP Pavilion x360")
                    .price(5400)
                    .quantity(12)
                    .selected(true)
                    .build());

            productRepository.save(Product.builder()
                    .id(UUID.randomUUID().toString())
                    .name("Samsung Galaxy Tab S9 Ultra")
                    .price(1300)
                    .quantity(23)
                    .build());

            productRepository.save(Product.builder()
                    .id(UUID.randomUUID().toString())
                    .name("Asus ROG Zephyrus G16")
                    .price(14500)
                    .quantity(8)
                    .selected(true)
                    .build());

            productRepository.save(Product.builder()
                    .id(UUID.randomUUID().toString())
                    .name("Canon EOS R10 Camera")
                    .price(1100)
                    .quantity(5)
                    .build());

            productRepository.save(Product.builder()
                    .id(UUID.randomUUID().toString())
                    .name("Câble USB-C vers HDMI")
                    .price(20)
                    .quantity(4)
                    .build());

            productRepository.save(Product.builder()
                    .id(UUID.randomUUID().toString())
                    .name("Clé USB 128 Go")
                    .price(40)
                    .quantity(2)
                    .build());

            productRepository.save(Product.builder()
                    .id(UUID.randomUUID().toString())
                    .name("Apple Watch Series 9")
                    .price(550)
                    .quantity(2)
                    .build());

            productRepository.save(Product.builder()
                    .id(UUID.randomUUID().toString())
                    .name("Xiaomi Redmi Note 13")
                    .price(250)
                    .quantity(5)
                    .selected(true)
                    .build());

            System.out.println("5 Produits disponibles dans la BD:");
            productRepository.findAll().stream().limit(5).forEach(p-> System.out.println(p.toString()));
        };
    }
}
