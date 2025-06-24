package com.example.bank.customer.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Customer statistics including total count and status breakdowns")
public class CustomerStatisticsResponse {

    @Schema(description = "Total number of customers in the system", example = "1200")
    private Long totalCustomers;

    @Schema(description = "Number of active customers", example = "950")
    private Long activeCustomers;

    @Schema(description = "Number of inactive customers", example = "100")
    private Long inactiveCustomers;

    @Schema(description = "Number of suspended customers", example = "50")
    private Long suspendedCustomers;

    @Schema(description = "Number of customers whose KYC is pending", example = "80")
    private Long kycPendingCustomers;

    @Schema(description = "Number of customers who have completed KYC", example = "1120")
    private Long kycCompletedCustomers;

}