package com.example.bank.customer.dtos;

import com.example.bank.customer.enums.CustomerStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description="Update customer status request")
public class CustomerStatusUpdateRequest {
  @Schema(description="the status to update")
  private CustomerStatus status;

  @Schema(description = "reason for the status update outcome")
  private String reason;
}
