package com.openclassrooms.paymybuddy.entity;

import jakarta.persistence.*;

@Entity
@Table
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Currency(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Currency () {
    }
}
