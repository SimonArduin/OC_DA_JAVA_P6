package com.openclassrooms.paymybuddy.entity;

import com.openclassrooms.paymybuddy.dto.UserDto;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "account_balance", nullable = false)
    private Double accountBalance = 0.0;

    @Column(name = "currency_id", nullable = false)
    private Integer currencyId = 1;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String iban;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(name = "role_id", nullable = false)
    private Integer roleId = 1;

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

    public User(Integer id, Double accountBalance, Integer currencyId, String email, String iban, String password, Integer roleId, String username, List<User> connections) {
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(Double accountBalance) {
        this.accountBalance = accountBalance;
    }

    public Integer getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Integer currencyId) {
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

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
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
        return (this.getUsername() == null
                || this.getPassword() == null
                || this.getEmail() == null);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return this.isEmpty();
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        User objUser = (User) obj;
        if(objUser.isEmpty() && this.isEmpty())
            return true;
        if (Objects.equals(objUser.getId(), this.getId())
                && Objects.equals(objUser.getAccountBalance(), this.getAccountBalance())
                && Objects.equals(objUser.getCurrencyId(), this.getCurrencyId())
                && Objects.equals(objUser.getEmail(), this.getEmail())
                && Objects.equals(objUser.getIban(), this.getIban())
                && Objects.equals(objUser.getPassword(), this.getPassword())
                && Objects.equals(objUser.getRoleId(), this.getRoleId())
                && Objects.equals(objUser.getUsername(), this.getUsername())) {
            if (objUser.getConnections() == null && this.getConnections() == null) {
                return true;
            }
            if (!(objUser.getConnections() != null && this.getConnections() != null)) {
                return false;
            }
            return objUser.getConnections().equals(this.getConnections());
        }
        return false;
    }
}
