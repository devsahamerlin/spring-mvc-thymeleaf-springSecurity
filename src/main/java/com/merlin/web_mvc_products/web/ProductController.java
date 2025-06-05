package com.merlin.web_mvc_products.web;

import com.merlin.web_mvc_products.entities.Product;
import com.merlin.web_mvc_products.repository.ProductRepository;
import groovy.util.logging.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.UUID;

@lombok.extern.slf4j.Slf4j
@Controller
@Slf4j
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/user/index")
    @PreAuthorize("hasRole('USER')")
    public String index(Model model,
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

        model.addAttribute("productList", products);
        model.addAttribute("keyword", keyword);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("minQuantity", minQuantity);

        return "products";
    }

    @GetMapping("/user/searchByPrice")
    @PreAuthorize("hasRole('USER')")
    public String searchByPrice(@RequestParam double minPrice,
                                @RequestParam double maxPrice,
                                Model model) {
        List<Product> products = productRepository.findByPriceBetween(minPrice, maxPrice);
        model.addAttribute("productList", products);
        model.addAttribute("searchType", "price");
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        return "products";
    }

    @GetMapping("/user/inStock")
    @PreAuthorize("hasRole('USER')")
    public String productsInStock(@RequestParam(defaultValue = "0") int threshold, Model model) {
        List<Product> products = productRepository.findByQuantityGreaterThan(threshold);
        model.addAttribute("productList", products);
        model.addAttribute("searchType", "stock");
        model.addAttribute("threshold", threshold);
        return "products";
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/user/index";
    }

    @PostMapping("/admin/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String delete(@RequestParam(name = "id") String id){
        productRepository.deleteById(id);
        return "redirect:/user/index";
    }

    @GetMapping("/admin/newProduct")
    @PreAuthorize("hasRole('ADMIN')")
    public String newProduct(Model model) {
        model.addAttribute("product", new Product());
        return "new-product";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/saveProduct")
    public String saveProduct(@Valid Product product, BindingResult bindingResult, Model model) {
        product.setId(UUID.randomUUID().toString());
        try {
            if(bindingResult.hasErrors()) {
                log.error("Error {}, saving product {}",bindingResult.getFieldError().getDefaultMessage(), product.toString());
                return "redirect:/admin/newProduct";
            }
            productRepository.save(product);

        } catch (Exception e) {
            log.error("Exception error {}, saving product {}",e.getMessage(), product.toString());
        }

        return "redirect:/user/index";
    }

    @GetMapping("/admin/editProduct")
    @PreAuthorize("hasRole('ADMIN')")
    public String editProduct(@RequestParam(name = "id") String id, Model model) {
        Product product = productRepository.findById(id).get();
        model.addAttribute("product", product);
        return "edit-product";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/updateProduct")
    public String updateProduct(@Valid Product product, BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()) return "edit-product";
        productRepository.save(product);
        return "redirect:/user/index";
    }

    @GetMapping("/notAuthorized")
    public String notAuthorized(){
        return "notAuthorized";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "login";
    }

}