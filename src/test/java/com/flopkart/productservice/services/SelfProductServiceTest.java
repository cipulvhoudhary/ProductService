package com.flopkart.productservice.services;

import com.flopkart.productservice.service.SelfProductService;
import com.flopkart.productservice.validations.ProductValidator;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.mockStatic;


@ExtendWith(MockitoExtension.class)
public class SelfProductServiceTest {

    @InjectMocks
    private SelfProductService selfProductService;

    private MockedStatic<ProductValidator> mockStatic;

    @Before("")
    public void setUp() {
        // Registering a static mock for UserService before each test
        mockStatic = mockStatic(ProductValidator.class);
    }

    @After("")
    public void tearDown() {
        // Closing the mockStatic after each test
        mockStatic.close();
    }

    @Test
    void testCreateProduct() {

    }
}
