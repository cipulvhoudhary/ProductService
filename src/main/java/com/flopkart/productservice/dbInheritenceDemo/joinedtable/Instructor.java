package com.flopkart.productservice.dbInheritenceDemo.joinedtable;


import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "jt_instructors")
public class Instructor extends User {

    private String subject;

    private Double rating;

}
