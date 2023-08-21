package com.openclassrooms.paymybuddy.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private Double account_balance = 0.0;

    @Column(name = "currency_id", nullable = false)
    private int currencyId = 1;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column
    private String iban;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(name = "role_id", nullable = false)
    private int roleId = 1;

    @Column(nullable = false, unique = true, length = 100)
    private String username;

    @ManyToMany
    @JoinTable(
            name = "user_user",
            joinColumns = @JoinColumn(name = "user_1_id"),
            inverseJoinColumns = @JoinColumn(name = "user_2_id")
    )
    private List<User> connections;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getAccount_balance() {
        return account_balance;
    }

    public void setAccount_balance(Double account_balance) {
        this.account_balance = account_balance;
    }

    public int getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(int currencyId) {
        this.currencyId = currencyId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<User> getConnections() {
        return connections;
    }

    public void setConnections(List<User> connections) {
        this.connections = connections;
    }

    public boolean addConnection(User connection) {
        if(!this.connections.contains(connection))
            return this.connections.add(connection);
        return false;
    }
}
