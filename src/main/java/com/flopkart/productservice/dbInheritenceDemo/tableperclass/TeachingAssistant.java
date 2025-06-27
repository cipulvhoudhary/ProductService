package com.flopkart.productservice.dbInheritenceDemo.tableperclass;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "tpc_teaching_assistants")
public class TeachingAssistant extends User {

    private int numberOfHRs;

}
