package com.example.bank.KYC.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.example.bank.KYC.enums.KycStatus;
import com.example.bank.common.dto.PaginationRequest;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Filter criteria for searching and retrieving kyc records.")
public class KycFilter extends PaginationRequest {

  @Parameter(description = "Filter by status (optional)")
  private KycStatus status;

  @Parameter(description = "Filter by creation date (format: yyyy-MM-dd)")
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) // auto-parses string to LocalDate
  private LocalDate createdAt;
}
