package com.example.bank.common.exception;

public class ValidationException extends RuntimeException {
        public ValidationException(String message) {
        super(message);
    }
}