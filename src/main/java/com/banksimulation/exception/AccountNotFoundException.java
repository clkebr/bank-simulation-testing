package com.banksimulation.exception;

public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException(String s) {
        super(s);
    }

}
