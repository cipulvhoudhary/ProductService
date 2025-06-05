package com.flopkart.productservice.controlleradvice;

import com.flopkart.productservice.dtos.ExceptionDto;
import com.flopkart.productservice.dtos.ProductNotFoundExceptionDto;
import com.flopkart.productservice.exceptions.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ProductServiceControllerAdvice {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionDto> handleProductNotFound() {
        ExceptionDto exceptionDto = new ExceptionDto();
        exceptionDto.setMessage("Product not found");
        exceptionDto.setResolutionDetails("Check product id");
        return new ResponseEntity<>(exceptionDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ProductNotFoundExceptionDto> handleProductNotFoundException(Exception e) {
        ProductNotFoundExceptionDto productNotFoundExceptionDto = new ProductNotFoundExceptionDto();
        String message = e.getMessage();
        System.out.println(message);
        
        // TODO: Extract product id from message and set it in ProductNotFoundExceptionDto
        
        productNotFoundExceptionDto.setProductId(1L);
        productNotFoundExceptionDto.setMessage("Product not found");
        productNotFoundExceptionDto.setResolutionDetails("Check product id");
        return new ResponseEntity<>(productNotFoundExceptionDto, HttpStatus.NOT_FOUND);
    }
}
