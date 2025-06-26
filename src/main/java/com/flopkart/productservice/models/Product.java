package com.flopkart.productservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "products")
public class Product extends BaseModel{

    private String title;

    private Double price;

    private String description;

    private String imageUrl;

    @ManyToOne
    private Category category;

}
