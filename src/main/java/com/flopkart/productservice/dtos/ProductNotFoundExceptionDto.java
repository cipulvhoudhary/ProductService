package com.flopkart.productservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductNotFoundExceptionDto {
    private Long productId;
    private String message;
    private String resolutionDetails;
}
