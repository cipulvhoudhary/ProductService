package com.flopkart.productservice.service;

import com.flopkart.productservice.exceptions.ProductNotFoundException;
import com.flopkart.productservice.models.Product;

import java.util.List;

public interface ProductService {

    public Product getProductById(Long productId) throws ProductNotFoundException;

    public List<Product> getAllProducts();

    public Product createProduct(Product product);

    public void deleteProduct(Long productId);

}
