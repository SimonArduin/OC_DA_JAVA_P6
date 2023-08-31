package com.openclassrooms.paymybuddy.dto;

public class PastTransactionDto {

    private Integer id;
    private String username;
    private String description;
    private Double amount;
    private String currency;

    public PastTransactionDto(Integer id, String username, String description, Double amount, String currency) {
        this.id = id;
        this.username = username;
        this.description = description;
        this.amount = amount;
        this.currency = currency;
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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public boolean isEmpty() {
        return (this.username == null
        || this.amount == null
        || this.description == null
        || this.currency == null);
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
        if (this.isEmpty() && objPastTransactionDto.isEmpty())
            return true;
        if (objPastTransactionDto.getId() == this.getId()
                && objPastTransactionDto.getAmount().equals(this.getAmount())
                && objPastTransactionDto.getDescription() == this.getDescription()
                && objPastTransactionDto.getCurrency() == this.getCurrency()
                && objPastTransactionDto.getUsername() == this.getUsername())
            return true;
        return false;
    }
}
