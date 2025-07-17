package com.example.bank.ledger.interfaces;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.bank.ledger.dtos.CreateLedgerEntryRequest;
import com.example.bank.ledger.dtos.DoubleEntryRequest;
import com.example.bank.ledger.dtos.LedgerEntryFilter;
import com.example.bank.ledger.dtos.LedgerEntryResponse;

public interface ILedgerEntry {
    
    LedgerEntryResponse createEntry(CreateLedgerEntryRequest request);
    
    List<LedgerEntryResponse> createDoubleEntry(DoubleEntryRequest request);
    
    LedgerEntryResponse getByEntryId(String entryId);
    
    List<LedgerEntryResponse> getByTransactionId(UUID transactionId);
    
    Page<LedgerEntryResponse> getEntries(LedgerEntryFilter filter);
    
    LedgerEntryResponse reverseEntry(String entryId, UUID reversedBy);
    
    List<LedgerEntryResponse> reverseTransaction(UUID originalTransactionId, UUID reversalTransactionId, UUID reversedBy);
    
    void validateTransaction(UUID transactionId);
}
