package com.merlin.web_mvc_products.repository;

import com.merlin.web_mvc_products.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, String> {
    List<Product> findByNameContainingIgnoreCase(String keyword);
    List<Product> findByPriceBetween(double minPrice, double maxPrice);
    List<Product> findByQuantityGreaterThan(double quantity);
    List<Product> findByNameContainingIgnoreCaseAndPriceBetween(String name, double minPrice, double maxPrice);
}
