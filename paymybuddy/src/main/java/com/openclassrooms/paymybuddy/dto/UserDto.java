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

    public UserDto(Integer id, Double accountBalance, Integer currencyId, String email, String iban, String password, Integer roleId, String username, List<UserDto> connections) {
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

    public UserDto(User user) {
        if (user == null || user.isEmpty())
            throw new IllegalArgumentException();
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

    public UserDto(UserDto userDto) {
        if (userDto == null || userDto.isEmpty())
            throw new IllegalArgumentException();
        else {
            this.id = userDto.getId();
            this.accountBalance = userDto.getAccountBalance();
            this.currencyId = userDto.getCurrencyId();
            this.email = userDto.getEmail();
            this.iban = userDto.getIban();
            this.password = userDto.getPassword();
            this.roleId = userDto.getRoleId();
            this.username = userDto.getUsername();
            this.connections = new ArrayList<>();
            if (userDto.getConnections() != null && !userDto.getConnections().isEmpty())
                for (UserDto connection : userDto.getConnections())
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
        return (this.getUsername() == null
                || this.getPassword() == null
                || this.getEmail() == null);
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
        if (objUserDto.isEmpty() && this.isEmpty())
            return true;
        if (objUserDto.getId() == this.getId()
                && objUserDto.getAccountBalance() == this.getAccountBalance()
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
            if (objUserDto.getConnections().equals(this.getConnections()))
                return true;
        }
            return false;
    }
}