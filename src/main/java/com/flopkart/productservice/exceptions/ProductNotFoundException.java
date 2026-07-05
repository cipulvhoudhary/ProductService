package com.flopkart.productservice.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductNotFoundException extends RuntimeException{

    private Long productId;

    public ProductNotFoundException() {}

    public ProductNotFoundException(Long productId) {
        this.productId = productId;
    }

    public ProductNotFoundException(Long productId, String message) {
        super(message);
        this.productId = productId;
    }

    public ProductNotFoundException(String message) {
        super(message);
    }

}
