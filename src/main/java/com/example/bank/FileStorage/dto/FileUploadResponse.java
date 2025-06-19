package com.example.bank.FileStorage.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response object for file upload operations")
public class FileUploadResponse {
    @Schema(description="Public ID of the uploaded file", example = "sample_public_id")
    private String publicId;

    @Schema(description="URL of the uploaded file", example = "https://res.cloudinary.com/demo/image/upload/v1616161616/sample_public_id.jpg")
    private String url;

    @Schema(description="Original file name", example = "sample_image.jpg")
    private String originalFileName;

    @Schema(description="File size in bytes", example = "204800")
    private long size;

    @Schema(description="content type of the uploaded file", example = "image/jpeg")
    private String contentType;

    @Schema(description="Timestamp when the file was uploaded", example = "2023-10-01T12:00:00Z")
    private LocalDateTime uploadedAt;

}
