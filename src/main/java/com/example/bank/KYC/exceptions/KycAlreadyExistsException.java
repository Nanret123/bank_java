package com.example.bank.KYC.exceptions;

public class KycAlreadyExistsException extends RuntimeException {
    public KycAlreadyExistsException(String message) {
        super(message);
    }
}