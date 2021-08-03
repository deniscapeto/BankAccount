package com.cockroachlabs.domain.entities;

import java.util.UUID;

public class Account {

    private UUID id;
    private Integer balance;

    public Account(Integer balance){
        this.id = UUID.randomUUID();
        this.balance = balance;
    }

    public UUID getId() {
        return id;
    }

    public Integer getBalance() {
        return balance;
    }
}
