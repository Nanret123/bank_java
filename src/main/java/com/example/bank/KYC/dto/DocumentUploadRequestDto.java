package com.example.bank.KYC.dto;

import org.springframework.web.multipart.MultipartFile;

import com.example.bank.KYC.enums.DocumentType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Payload for uploading a KYC document")
public class DocumentUploadRequestDto {
    @NotNull(message = "Document type is required")
    private DocumentType documentType;

    @Schema(description = "Upload of ID document")
    @NotNull(message = "document required for verification")
    private MultipartFile document;
}