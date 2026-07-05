package com.flopkart.productservice.service;

import com.flopkart.productservice.models.Product;

import java.util.List;

public interface ProductService {

    Product getProductById(Long productId);

    List<Product> getAllProducts();

    Product createProduct(Product product);

    void deleteProduct(Long productId);

}
