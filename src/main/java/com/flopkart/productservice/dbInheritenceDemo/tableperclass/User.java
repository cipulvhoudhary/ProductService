package com.flopkart.productservice.dbInheritenceDemo.tableperclass;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "tpc_users")
@Inheritance(strategy = jakarta.persistence.InheritanceType.TABLE_PER_CLASS)
public class User {

    @Id
    private Long id;

    private String name;

    private String email;

    private String password;
}
