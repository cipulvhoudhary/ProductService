package com.flopkart.productservice.controller;

import com.flopkart.productservice.models.Product;
import com.flopkart.productservice.service.ProductService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(@Qualifier("selfProductService") ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id") Long productId) {
        return new ResponseEntity<>(
                productService.getProductById(productId),
                HttpStatus.OK);
    }

    @GetMapping("/")
    public Iterable<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @PostMapping()
    public Product saveProduct(@RequestBody Product product) {
        return productService.createProduct(product);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable("id") Long productId) {
        productService.deleteProduct(productId);
    }

}
