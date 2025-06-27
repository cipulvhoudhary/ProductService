package com.flopkart.productservice.service;

import com.flopkart.productservice.exceptions.CategoryNotFoundException;
import com.flopkart.productservice.exceptions.ProductNotFoundException;
import com.flopkart.productservice.models.Category;
import com.flopkart.productservice.models.Product;
import com.flopkart.productservice.repositories.CategoryRepository;
import com.flopkart.productservice.repositories.ProductRepository;
import com.flopkart.productservice.validations.ProductValidator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("selfProductService")
public class SelfProductService implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public SelfProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Product getProductById(Long productId) throws ProductNotFoundException {
        return productRepository.findById(productId).orElseThrow(() ->
                new ProductNotFoundException(productId, "Product with id " + productId + " not found"));
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product createProduct(Product product) {

        ProductValidator.isValidProduct(product);

        Optional<Category> categoryOptional = categoryRepository.findByTitle(product.getCategory().getTitle());

        Category categoryForDb = categoryOptional.orElseGet(() -> categoryRepository.save(getCategoryForDb(product.getCategory())));

        Product productForDb = getProductForDb(product, categoryForDb);

        return productRepository.save(productForDb);

    }

    @Override
    public void deleteProduct(Long productId) throws ProductNotFoundException {
        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException(productId, "The product with Id: " + productId + " does not exists");
        }
        productRepository.deleteById(productId);
    }

    private static Product getProductForDb(Product product, Category categoryForDb) {
        return Product.builder()
                .title(product.getTitle())
                .price(product.getPrice())
                .description(product.getDescription())
                .imageUrl(product.getImageUrl())
                .category(categoryForDb)
                .build();
    }

    private static Category getCategoryForDb(Category category) {
        return Category.builder()
                .title(category.getTitle())
                .build();
    }

}
