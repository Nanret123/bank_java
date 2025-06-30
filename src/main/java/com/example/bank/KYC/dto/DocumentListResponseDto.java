package com.example.bank.KYC.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response wrapper for a list of customer's uploaded KYC documents")
public class DocumentListResponseDto {
    private List<KycDocumentDto> documents;
    private int totalCount;
}