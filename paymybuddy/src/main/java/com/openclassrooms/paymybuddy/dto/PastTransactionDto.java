package com.openclassrooms.paymybuddy.dto;

public class PastTransactionDto {

    private Integer id;
    private String username;
    private String description;
    private Double amount;
    private String currencySymbol;

    public PastTransactionDto(Integer id, String username, String description, Double amount, String currencySymbol) {
        this.id = id;
        this.username = username;
        this.description = description;
        this.amount = amount;
        this.currencySymbol = currencySymbol;
    }

    public PastTransactionDto() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public boolean isEmpty() {
        return (this.username == null
        || this.amount == null
        || this.description == null
        || this.currencySymbol == null);
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
        PastTransactionDto objPastTransactionDto = (PastTransactionDto) obj;
        if (this.isEmpty()) {
            if (objPastTransactionDto.isEmpty())
                return true;
        } else if (this.getId().equals(objPastTransactionDto.getId())
                && this.getAmount().equals(objPastTransactionDto.getAmount())
                && this.getDescription().equals(objPastTransactionDto.getDescription())
                && this.getCurrencySymbol().equals(objPastTransactionDto.getCurrencySymbol())
                && this.getUsername().equals(objPastTransactionDto.getUsername()))
            return true;
        return false;
    }
}
