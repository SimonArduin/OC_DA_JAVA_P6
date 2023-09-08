package com.openclassrooms.paymybuddy.dto;

import com.openclassrooms.paymybuddy.entity.User;

import java.util.ArrayList;
import java.util.List;

public class UserDto {
    private Integer id;

    private Double accountBalance = 0.0;

    private Integer currencyId = 1;

    private String email;

    private String iban;

    private String password;

    private Integer roleId = 1;

    private String username;

    private List<UserDto> connections = new ArrayList<>();

    public UserDto() {
    }

    public UserDto(User user) {
        if (user == null || user.isEmpty())
            throw new IllegalArgumentException("Invalid user");
        else {
            this.id = user.getId();
            this.accountBalance = user.getAccountBalance();
            this.currencyId = user.getCurrencyId();
            this.email = user.getEmail();
            this.iban = user.getIban();
            this.password = user.getPassword();
            this.roleId = user.getRoleId();
            this.username = user.getUsername();
            this.connections = new ArrayList<>();
            if (user.getConnections() != null && !user.getConnections().isEmpty())
                for (User connection : user.getConnections())
                    this.connections.add(new UserDto(connection));
        }
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

    public List<UserDto> getConnections() {
        return connections;
    }

    public void setConnections(List<UserDto> connections) {
        this.connections = connections;
    }

    public boolean addConnection(UserDto connection) {
        if(this.connections==null)
            this.connections = new ArrayList<>();
        if(connection!=null && !connection.isEmpty() && !this.equals(connection) && !this.connections.contains(connection))
            return this.connections.add(connection);
        return false;
    }

    public boolean isEmpty() {
        return (this.id == null
                || this.accountBalance == null
                || this.currencyId == null
                || this.email == null
                || this.iban == null
                || this.password == null
                || this.roleId == null
                || this.username == null
                || this.connections == null);
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
        UserDto objUserDto = (UserDto) obj;
        if (this.isEmpty()) {
            if (objUserDto.isEmpty())
                return true;
            return false;
        } else if (this.getId().equals(objUserDto.getId())
                && this.getAccountBalance().equals(objUserDto.getAccountBalance())
                && this.getCurrencyId().equals(objUserDto.getCurrencyId())
                && this.getEmail().equals(objUserDto.getEmail())
                && this.getIban().equals(objUserDto.getIban())
                && this.getPassword().equals(objUserDto.getPassword())
                && this.getRoleId().equals(objUserDto.getRoleId())
                && this.getUsername().equals(objUserDto.getUsername())) {
            if (this.getConnections() == null && objUserDto.getConnections() == null)
                return true;
            if (!(this.getConnections() != null && objUserDto.getConnections() != null))
                return false;
            if (this.getConnections().equals(objUserDto.getConnections()))
                return true;
        }
            return false;
    }
}