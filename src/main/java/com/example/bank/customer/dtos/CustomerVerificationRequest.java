package com.example.bank.customer.dtos;

import java.util.UUID;

import com.example.bank.customer.enums.VerificationStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description="Verify customer details")
public class CustomerVerificationRequest {
    @NotNull(message = "Customer ID is required")
    private UUID customerId;

    @NotNull(message = "Verification status is required")
    private VerificationStatus verificationStatus;
}
