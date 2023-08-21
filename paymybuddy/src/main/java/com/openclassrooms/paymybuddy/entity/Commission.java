package com.openclassrooms.paymybuddy.entity;

import jakarta.persistence.*;

@Entity
@Table
public class Commission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private float rate;
}
