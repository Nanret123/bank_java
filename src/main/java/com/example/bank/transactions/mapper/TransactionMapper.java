package com.example.bank.transactions.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.example.bank.transactions.dtos.DepositRequest;
import com.example.bank.transactions.dtos.TransactionResponse;
import com.example.bank.transactions.dtos.TransferRequest;
import com.example.bank.transactions.dtos.WithdrawalRequest;
import com.example.bank.transactions.entity.Transaction;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TransactionMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fromAccountNumber", ignore = true)
    @Mapping(target = "toAccountNumber", source = "request.accountNumber")
    @Mapping(target = "status", constant = "PENDING")
    @Mapping(target = "transactionDate", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "valueDate", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "approvedBy", ignore = true)
    @Mapping(target = "reversalReference", ignore = true)
    @Mapping(target = "transactionFee", ignore = true)
    @Mapping(target = "vatAmount", ignore = true)
    Transaction depositRequestToTransaction(DepositRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "transactionType", constant = "WITHDRAWAL")
    @Mapping(target = "fromAccountNumber", source = "accountNumber")
    @Mapping(target = "toAccountNumber", ignore = true)
    @Mapping(target = "status", constant = "PENDING")
    @Mapping(target = "transactionDate", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "valueDate", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "approvedBy", ignore = true)
    @Mapping(target = "reversalReference", ignore = true)
    @Mapping(target = "transactionFee", ignore = true)
    @Mapping(target = "vatAmount", ignore = true)
    Transaction withdrawalRequestToTransaction(WithdrawalRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "transactionType", constant = "TRANSFER")
    @Mapping(target = "status", constant = "PENDING")
    @Mapping(target = "transactionDate", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "valueDate", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "approvedBy", ignore = true)
    @Mapping(target = "reversalReference", ignore = true)
    @Mapping(target = "transactionFee", ignore = true)
    @Mapping(target = "vatAmount", ignore = true)
    Transaction toTransferTransaction(TransferRequest request);

    TransactionResponse toResponse(Transaction transaction);
}