package com.flopkart.productservice.controllerAdvice;

import com.flopkart.productservice.dtos.ErrorResponse;
import com.flopkart.productservice.dtos.ExceptionDto;
import com.flopkart.productservice.dtos.ProductNotFoundExceptionDto;
import com.flopkart.productservice.exceptions.CategoryNotFoundException;
import com.flopkart.productservice.exceptions.ProductNotFoundException;
import com.flopkart.productservice.exceptions.UnauthorisedException;
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
    public ResponseEntity<ProductNotFoundExceptionDto> handleProductNotFoundException(ProductNotFoundException e) {
        ProductNotFoundExceptionDto productNotFoundExceptionDto = new ProductNotFoundExceptionDto();
        productNotFoundExceptionDto.setProductId(e.getProductId());
        productNotFoundExceptionDto.setMessage("Product not found");
        productNotFoundExceptionDto.setResolutionDetails("Check product id");
        return new ResponseEntity<>(productNotFoundExceptionDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ExceptionDto> handleCategoryNotFound() {
        ExceptionDto exceptionDto = new ExceptionDto();
        exceptionDto.setMessage("Category not found");
        exceptionDto.setResolutionDetails("Check category id");
        return new ResponseEntity<>(exceptionDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnauthorisedException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UnauthorisedException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(ex.getMessage()));
    }
}

