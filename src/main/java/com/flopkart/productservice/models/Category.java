package com.flopkart.productservice.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;

@Getter
@Setter
@Builder
@Entity(name = "categories")
@NoArgsConstructor
@AllArgsConstructor
public class Category extends BaseModel {

    @Column(unique = true, nullable = false)
    private String title;

}