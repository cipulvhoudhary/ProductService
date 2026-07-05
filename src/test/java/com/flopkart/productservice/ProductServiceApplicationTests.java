package com.flopkart.productservice;

import com.flopkart.productservice.models.Product;
import com.flopkart.productservice.repositories.ProductRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductServiceApplicationTests {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void contextLoads() {
    }

    @Test
    @Disabled
    void testProductRepository() {
        System.out.println(productRepository);

        Product product = (Product) productRepository.findAllProductByIdSql();

        assert product == null;
    }

}
