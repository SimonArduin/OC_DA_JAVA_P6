package com.openclassrooms.paymybuddy.dto;

import com.openclassrooms.paymybuddy.entity.User;
import jakarta.persistence.*;

import java.util.List;

public class UserDto {
    private int id;

    private Double account_balance = 0.0;

    private int currencyId = 1;

    private String email;

    private String iban;

    private String password;

    private int roleId = 1;

    private String username;

    private List<UserDto> connections;

    public UserDto() {
    }

    public UserDto(int id, Double account_balance, int currencyId, String email, String iban, String password, int roleId, String username, List<UserDto> connections) {
        this.id = id;
        this.account_balance = account_balance;
        this.currencyId = currencyId;
        this.email = email;
        this.iban = iban;
        this.password = password;
        this.roleId = roleId;
        this.username = username;
        this.connections = connections;
    }

    public UserDto(User user) {
        if (user == null || user.isEmpty())
            throw new IllegalArgumentException();
        else {
            this.id = user.getId();
            this.account_balance = user.getAccount_balance();
            this.currencyId = user.getCurrencyId();
            this.email = user.getEmail();
            this.iban = user.getIban();
            this.password = user.getPassword();
            this.roleId = user.getRoleId();
            this.username = user.getUsername();
            this.connections = null;
            if (user.getConnections() != null && !user.getConnections().isEmpty())
                for (User connection : user.getConnections())
                    this.connections.add(new UserDto(connection));
        }
    }

    public UserDto(UserDto userDto) {
        if (userDto == null || userDto.isEmpty())
            throw new IllegalArgumentException();
        else {
            this.id = userDto.getId();
            this.account_balance = userDto.getAccount_balance();
            this.currencyId = userDto.getCurrencyId();
            this.email = userDto.getEmail();
            this.iban = userDto.getIban();
            this.password = userDto.getPassword();
            this.roleId = userDto.getRoleId();
            this.username = userDto.getUsername();
            this.connections = null;
            if (userDto.getConnections() != null && !userDto.getConnections().isEmpty())
                for (UserDto connection : userDto.getConnections())
                    this.connections.add(new UserDto(connection));
        }
    }

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

    public List<UserDto> getConnections() {
        return connections;
    }

    public void setConnections(List<UserDto> connections) {
        this.connections = connections;
    }

    public boolean addConnection(UserDto connection) {
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
            return false;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        UserDto objUserDto = (UserDto) obj;
        if (objUserDto.getId() == this.getId()
                && objUserDto.getAccount_balance() == this.getAccount_balance()
                && objUserDto.getCurrencyId() == this.getCurrencyId()
                && objUserDto.getEmail() == this.getEmail()
                && objUserDto.getIban() == this.getIban()
                && objUserDto.getPassword() == this.getPassword()
                && objUserDto.getRoleId() == this.getRoleId()
                && objUserDto.getUsername() == this.getUsername()) {
            if (objUserDto.getConnections() == null && this.getConnections() == null)
                return true;
            if (!(objUserDto.getConnections() != null && this.getConnections() != null))
                return false;
            if(objUserDto.getConnections() == this.getConnections())
                return true;
        }
            return false;
    }
}