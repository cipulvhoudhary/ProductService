package com.flopkart.productservice.controller;

import com.flopkart.productservice.exceptions.ProductNotFoundException;
import com.flopkart.productservice.models.Product;
import com.flopkart.productservice.service.ProductService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductController {

    ProductService productService;

    public ProductController(@Qualifier("fakeStoreProductService") ProductService productService) {
        this.productService = productService;
    }

    // localhost8080/product/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id") Long productId) throws ProductNotFoundException {

        //        ResponseEntity<Product> responseEntity = null;
//        try {
//            Product product = productService.getProductById(productId);
//            responseEntity = new ResponseEntity<>(product, HttpStatus.OK);
//        } catch (Exception e) {
//            responseEntity = new ResponseEntity<>(HttpStatus.NOT_FOUND);
//            e.printStackTrace();
//        }
        return new ResponseEntity<>(
                productService.getProductById(productId),
                HttpStatus.OK);
    }

    // localhost8080/product/
    @GetMapping("/")
    public Iterable<Product> getAllProducts() {
        return productService.getAllProducts();
    }


    public Product saveProduct(@RequestBody Product product) {
        return new Product();
    }

    public void deleteProduct(@PathVariable("id") Long productId) {
        return;
    }

}
