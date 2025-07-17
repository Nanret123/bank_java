package com.example.bank.ledger.mapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.bank.ledger.dtos.CreateLedgerEntryRequest;
import com.example.bank.ledger.dtos.LedgerEntryResponse;
import com.example.bank.ledger.entity.LedgerEntry;
import com.example.bank.ledger.enums.EntryType;

@Mapper(componentModel = "spring")
public interface LedgerEntryMapper {

  LedgerEntryResponse toResponse(LedgerEntry ledgerEntry);

  List<LedgerEntryResponse> toResponseList(List<LedgerEntry> ledgerEntries);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "entryId", ignore = true)
  @Mapping(target = "status", constant = "PENDING")
  @Mapping(target = "reversed", constant = "false")
  @Mapping(target = "reversalEntryId", ignore = true)
  @Mapping(target = "originalEntryId", ignore = true)
  @Mapping(target = "version", ignore = true)
  LedgerEntry toEntity(CreateLedgerEntryRequest request);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "entryId", ignore = true)
  @Mapping(target = "status", constant = "PENDING")
  @Mapping(target = "reversed", constant = "false")
  @Mapping(target = "reversalEntryId", ignore = true)
  @Mapping(target = "originalEntryId", ignore = true)
  @Mapping(target = "version", ignore = true)
  @Mapping(target = "entryType", source = "entryType")
  @Mapping(target = "accountNumber", source = "accountNumber")
  LedgerEntry createEntry(UUID transactionId, String accountNumber,
      EntryType entryType, BigDecimal amount,
      String createdBy, LocalDateTime entryDate);
}