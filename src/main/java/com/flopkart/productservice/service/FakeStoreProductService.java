package com.flopkart.productservice.service;

import com.flopkart.productservice.dtos.FakeStoreProductDto;
import com.flopkart.productservice.exceptions.ProductNotFoundException;
import com.flopkart.productservice.models.Category;
import com.flopkart.productservice.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service("fakeStoreProductService")
public class FakeStoreProductService implements ProductService{

    RestTemplate restTemplate;
    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    public FakeStoreProductService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Product getProductById(Long productId) throws ProductNotFoundException {
        ResponseEntity<FakeStoreProductDto> fakeStoreProductDto =  restTemplate.getForEntity(
                "https://fakestoreapi.com/products/" + productId,
                FakeStoreProductDto.class);

        FakeStoreProductDto fakeStoreProduct = fakeStoreProductDto.getBody();

        if (fakeStoreProduct == null) {
            throw new ProductNotFoundException("Product with id " + productId + " does not exist");
        }

        return getProductFromFakeStoreProductDto(fakeStoreProduct);
    }

    @Override
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();

        ResponseEntity<FakeStoreProductDto[]> fakeStoreProductDtos = restTemplate.getForEntity(
                "https://fakestoreapi.com/products",
                FakeStoreProductDto[].class
                );

        FakeStoreProductDto[] fakeStoreProducts = fakeStoreProductDtos.getBody();

        if (fakeStoreProducts == null) {
            return products;
        }

        for (FakeStoreProductDto fakeStoreProduct : fakeStoreProducts) {
            Product product = getProductFromFakeStoreProductDto(fakeStoreProduct);
            products.add(product);
        }
        return products;
    }

    @Override
    public Product createProduct(Product product) {
        return null;
    }

    @Override
    public void deleteProduct(Long productId) {

    }

    public Product getProductFromFakeStoreProductDto(FakeStoreProductDto fakeStoreProductDto) {
        if (fakeStoreProductDto == null) {
            return null;
        }

        Product product = new Product();
        product.setId(fakeStoreProductDto.getId());
        product.setTitle(fakeStoreProductDto.getTitle());
        product.setPrice(fakeStoreProductDto.getPrice());
        product.setDescription(fakeStoreProductDto.getDescription());
        product.setImageUrl(fakeStoreProductDto.getImage());
        Category category = new Category();
        category.setTitle(fakeStoreProductDto.getCategory());
        product.setCategory(category);

        return product;
    }
}
