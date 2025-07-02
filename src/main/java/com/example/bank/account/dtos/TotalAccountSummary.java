package com.example.bank.account.dtos;

import java.math.BigDecimal;
import java.util.Map;

import com.example.bank.account.enums.AccountStatus;
import com.example.bank.account.enums.AccountType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TotalAccountSummary {
    private Long totalAccounts;
    private BigDecimal totalBalance;
    private Map<AccountType, AccountTypeSummary> summaryByType;
    private Map<AccountStatus, Long> summaryByStatus;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AccountTypeSummary {
        private Long count;
        private BigDecimal totalBalance;
        private BigDecimal averageBalance;
    }
}
