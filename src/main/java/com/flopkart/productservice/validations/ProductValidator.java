package com.flopkart.productservice.validations;

import com.flopkart.productservice.models.Product;

public class ProductValidator {

    public static void isValidProduct(Product product) {

        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }

        if (product.getCategory() == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }

        if (product.getCategory().getTitle() == null) {
            throw new IllegalArgumentException("Category title cannot be null");
        }

    }

}
