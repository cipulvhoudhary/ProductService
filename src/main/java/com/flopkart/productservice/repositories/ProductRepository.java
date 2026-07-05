package com.flopkart.productservice.repositories;

import com.flopkart.productservice.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Override
    Optional<Product> findById(Long aLong);

    @Override
    void deleteById(Long productId);

//    @Query("SELECT p FROM Product p WHERE p.id = :productId")
//    Product findProductByIdHql(Long productId);

    @Query(value = "SELECT * FROM products WHERE id = :productId", nativeQuery = true)
    Product findProductByIdSql(Long productId);

    @Query(value = "SELECT * FROM products", nativeQuery = true)
    List<Product> findAllProductByIdSql();

}
