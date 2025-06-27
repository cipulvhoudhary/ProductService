package com.flopkart.productservice.dbInheritenceDemo.mappedsuperclass;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class TeachingAssistant extends User {

    private int numberOfHRs;

}
