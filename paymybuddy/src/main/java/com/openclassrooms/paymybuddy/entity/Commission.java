package com.openclassrooms.paymybuddy.entity;

import jakarta.persistence.*;

@Entity
@Table
public class Commission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private double rate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public Commission(Integer id, double rate) {
        this.id = id;
        this.rate = rate;
    }

    public Commission() {
    }
}
