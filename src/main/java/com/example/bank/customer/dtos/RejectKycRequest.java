package com.example.bank.customer.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description="Reject Customer Kyc")
public class RejectKycRequest {
@Schema(description="Reason why the Kyc was rejected", example="Images are not clear")
 private String reason;
}
