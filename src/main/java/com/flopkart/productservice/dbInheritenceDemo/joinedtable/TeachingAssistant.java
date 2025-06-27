package com.flopkart.productservice.dbInheritenceDemo.joinedtable;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "jt_teaching_assistants")
public class TeachingAssistant extends User {

    private int numberOfHRs;
}
