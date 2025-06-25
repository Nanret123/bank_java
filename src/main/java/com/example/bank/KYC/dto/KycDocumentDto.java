package com.example.bank.KYC.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.bank.KYC.enums.DocumentType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO representing a KYC document")
public class KycDocumentDto {
  @NotBlank(message = "ID is required")
  @Schema(description = "Unique identifier for the document", example = "1")
  private UUID id;

  @NotBlank(message = "File name is required")
  @Schema(description = "Original name of the uploaded file", example = "passport_photo.jpg")
  private String fileName;

  @NotBlank(message = "Cloudinary URL is required")
  @Schema(description = "Public URL to the file on Cloudinary", example = "https://res.cloudinary.com/.../passport_photo.jpg")
  private String cloudinaryUrl;

  @NotBlank(message = "File type is required")
  @Schema(description = "Type of file (e.g., image/jpeg, application/pdf)", example = "image/jpeg")
  private String fileType;

  @NotNull(message = "File size is required")
  @Schema(description = "Size of the file in bytes", example = "204800")
  private Long fileSize;

  @NotNull(message = "Document type is required")
  @Schema(description = "Type of document being submitted", example = "UTILITY_BILL")
  private DocumentType documentType;
}