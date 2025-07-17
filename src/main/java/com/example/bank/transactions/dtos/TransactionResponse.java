package com.example.bank.transactions.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.example.bank.transactions.enums.TransactionStatus;
import com.example.bank.transactions.enums.TransactionType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionResponse {
    private UUID id;
    private String transactionReference;
    private TransactionType transactionType;
    private String fromAccountNumber;
    private String toAccountNumber;
    private BigDecimal amount;
    private String narration;
    private TransactionStatus status;
    private LocalDateTime transactionDate;
    private LocalDateTime valueDate;
}