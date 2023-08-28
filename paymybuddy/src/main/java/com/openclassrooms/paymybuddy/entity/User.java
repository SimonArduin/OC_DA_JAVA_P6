package com.openclassrooms.paymybuddy.entity;

import com.openclassrooms.paymybuddy.dto.UserDto;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private Double accountBalance = 0.0;

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
    private List<User> connections = new ArrayList<>();

    public User() {
    }

    public User(int id, Double accountBalance, int currencyId, String email, String iban, String password, int roleId, String username, List<User> connections) {
        this.id = id;
        this.accountBalance = accountBalance;
        this.currencyId = currencyId;
        this.email = email;
        this.iban = iban;
        this.password = password;
        this.roleId = roleId;
        this.username = username;
        this.connections = connections;
    }

    public User(UserDto userDto) {
        this.id = userDto.getId();
        this.accountBalance = userDto.getAccountBalance();
        this.currencyId = userDto.getCurrencyId();
        this.email = userDto.getEmail();
        this.iban = userDto.getIban();
        this.password = userDto.getPassword();
        this.roleId = userDto.getRoleId();
        this.username = userDto.getUsername();
        this.connections = new ArrayList<>();
        if(userDto.getConnections() != null && !userDto.getConnections().isEmpty())
            for(UserDto connection : userDto.getConnections())
                this.connections.add(new User(connection));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(Double accountBalance) {
        this.accountBalance = accountBalance;
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
        if(this.connections==null)
            this.connections = new ArrayList<>();
        if(connection!=null && !connection.isEmpty() && !this.equals(connection) && !this.connections.contains(connection))
            return this.connections.add(connection);
        return false;
    }

    public boolean isEmpty() {
        return (this.getUsername() == null || this.getPassword() == null || this.getEmail() == null);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            if (this.isEmpty())
                return true;
            return false;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        User objUser = (User) obj;
        if(objUser.isEmpty() && this.isEmpty())
            return true;
        if (objUser.getId() == this.getId()
                && objUser.getAccountBalance() == this.getAccountBalance()
                && objUser.getCurrencyId() == this.getCurrencyId()
                && objUser.getEmail() == this.getEmail()
                && objUser.getIban() == this.getIban()
                && objUser.getPassword() == this.getPassword()
                && objUser.getRoleId() == this.getRoleId()
                && objUser.getUsername() == this.getUsername()) {
            if (objUser.getConnections() == null && this.getConnections() == null)
                return true;
            if (!(objUser.getConnections() != null && this.getConnections() != null))
                return false;
            if (objUser.getConnections().equals(this.getConnections()))
                return true;
        }
        return false;
    }
}
