package com.example.bank.FileStorage.dto;

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
@Schema(description = "Multiple file upload response")
public class MultipleFileUploadResponse {
    
    @Schema(description = "List of uploaded files")
    private List<FileUploadResponse> files;
    
    @Schema(description = "Total files uploaded", example = "3")
    private int totalUploaded;
    
}