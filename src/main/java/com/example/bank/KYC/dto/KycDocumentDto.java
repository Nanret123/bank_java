package com.example.bank.KYC.dto;

import java.time.LocalDateTime;

import com.example.bank.KYC.enums.DocumentType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KycDocumentDto {
  private Long id;
  private String fileName;
  private String cloudinaryUrl;
  private String fileType;
  private Long fileSize;
  private DocumentType documentType;
  private LocalDateTime uploadedAt;
}