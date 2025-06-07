package com.merlin.web_mvc_products.web;

import com.merlin.web_mvc_products.entities.Product;
import com.merlin.web_mvc_products.repository.ProductRepository;
import groovy.util.logging.Slf4j;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@lombok.extern.slf4j.Slf4j
@RestController
@RequestMapping("/api/products")
@Slf4j
@CrossOrigin("*")
public class ProductRestController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(
            @RequestParam(name = "keyword", defaultValue = "") String keyword,
            @RequestParam(name = "minPrice", required = false) Double minPrice,
            @RequestParam(name = "maxPrice", required = false) Double maxPrice,
            @RequestParam(name = "minQuantity", required = false) Integer minQuantity) {

        List<Product> products;

        if (minPrice != null && maxPrice != null && !keyword.isEmpty()) {
            products = productRepository.findByNameContainingIgnoreCaseAndPriceBetween(keyword, minPrice, maxPrice);
        } else if (minPrice != null && maxPrice != null) {
            products = productRepository.findByPriceBetween(minPrice, maxPrice);
        } else if (minQuantity != null) {
            products = productRepository.findByQuantityGreaterThan(minQuantity);
        } else if (!keyword.isEmpty()) {
            products = productRepository.findByNameContainingIgnoreCase(keyword);
        } else {
            products = productRepository.findAll();
        }

        return ResponseEntity.ok(products);
    }

    @GetMapping("/search/price")
    public ResponseEntity<List<Product>> searchByPrice(
            @RequestParam double minPrice,
            @RequestParam double maxPrice) {

        List<Product> products = productRepository.findByPriceBetween(minPrice, maxPrice);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/search/stock")
    public ResponseEntity<List<Product>> getProductsInStock(
            @RequestParam(defaultValue = "0") int threshold) {

        List<Product> products = productRepository.findByQuantityGreaterThan(threshold);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable String id) {
        Optional<Product> product = productRepository.findById(id);

        if (product.isPresent()) {
            return ResponseEntity.ok(product.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@Valid @RequestBody Product product, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("Validation error: {}", bindingResult.getFieldError().getDefaultMessage());
            return ResponseEntity.badRequest().body("Validation failed: " + bindingResult.getFieldError().getDefaultMessage());
        }

        try {
            product.setId(UUID.randomUUID().toString());
            Product savedProduct = productRepository.save(product);
            log.info("Product created successfully: {}", savedProduct.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
        } catch (Exception e) {
            log.error("Error creating product: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating product: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable String id,
                                           @Valid @RequestBody Product product,
                                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("Validation error: {}", bindingResult.getFieldError().getDefaultMessage());
            return ResponseEntity.badRequest().body("Validation failed: " + bindingResult.getFieldError().getDefaultMessage());
        }

        if (!productRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        try {
            product.setId(id); // Ensure the ID matches the path parameter
            Product updatedProduct = productRepository.save(product);
            log.info("Product updated successfully: {}", updatedProduct.getId());
            return ResponseEntity.ok(updatedProduct);
        } catch (Exception e) {
            log.error("Error updating product: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating product: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable String id) {
        if (!productRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        try {
            productRepository.deleteById(id);
            log.info("Product deleted successfully: {}", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error deleting product: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting product: " + e.getMessage());
        }
    }
}