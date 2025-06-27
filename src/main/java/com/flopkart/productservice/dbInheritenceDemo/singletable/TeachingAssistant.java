package com.flopkart.productservice.dbInheritenceDemo.singletable;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "st_teaching_assistants")
@DiscriminatorValue("3")
public class TeachingAssistant extends User {

    private int numberOfHRs;

}
