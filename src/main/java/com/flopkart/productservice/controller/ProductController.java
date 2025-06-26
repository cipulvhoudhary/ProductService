package com.flopkart.productservice.controller;

import com.flopkart.productservice.exceptions.CategoryNotFoundException;
import com.flopkart.productservice.exceptions.ProductNotFoundException;
import com.flopkart.productservice.models.Product;
import com.flopkart.productservice.service.ProductService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    ProductService productService;

    public ProductController(@Qualifier("selfProductService") ProductService productService) {
        this.productService = productService;
    }

    // localhost8080/product/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id") Long productId) throws ProductNotFoundException {
        return new ResponseEntity<>(
                productService.getProductById(productId),
                HttpStatus.OK);
    }

    // localhost8080/products/
    @GetMapping("/")
    public Iterable<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    // localhost8080/products/
    @PostMapping()
    public Product saveProduct(@RequestBody Product product) throws CategoryNotFoundException {
        return productService.createProduct(product);
    }

    // localhost8080/products/id
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable("id") Long productId) throws ProductNotFoundException {
        productService.deleteProduct(productId);
    }

}
