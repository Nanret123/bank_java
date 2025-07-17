package com.example.bank.transactions.interfaces;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.domain.Page;

import com.example.bank.transactions.dtos.AccountTransactionFilter;
import com.example.bank.transactions.dtos.DepositRequest;
import com.example.bank.transactions.dtos.TransactionFilter;
import com.example.bank.transactions.dtos.TransactionResponse;
import com.example.bank.transactions.dtos.TransferRequest;
import com.example.bank.transactions.dtos.WithdrawalRequest;

public interface ITransactionService {

  // Core transaction operations
  TransactionResponse processDeposit(DepositRequest request, UUID initiatedBy);

  TransactionResponse processWithdrawal(WithdrawalRequest request, UUID initiatedBy);

  TransactionResponse processTransfer(TransferRequest request, UUID initiatedBy);

  // // Transaction inquiry
  TransactionResponse getTransactionById(UUID transactionId);

  TransactionResponse getTransactionByReference(String transactionReference);

  Page<TransactionResponse> getTransactions(TransactionFilter filter);

  Page<TransactionResponse> getTransactionsForAccount(AccountTransactionFilter filter);

  Page<TransactionResponse> getTransactionHistory(AccountTransactionFilter request);

  // // Transaction management
  TransactionResponse approveTransaction(UUID id, UUID approvedBy);

  TransactionResponse reverseTransaction(UUID transactionId, UUID reversedBy);

  // // Reports
  Map<String, Object> getDailyTransactionSummary(String date);
}