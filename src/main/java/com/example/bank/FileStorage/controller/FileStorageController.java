package com.example.bank.FileStorage.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.bank.FileStorage.Service.FileStorageService;
import com.example.bank.FileStorage.dto.FileUploadResponse;
import com.example.bank.FileStorage.dto.MultipleFileUploadResponse;
import com.example.bank.common.dto.ApiResponseDto;
import com.example.bank.common.exception.InvalidFileException;
import com.example.bank.common.util.ApiResponseUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/files")
@Tag(name = "File Storage", description = "APIs for file storage operations")
@RequiredArgsConstructor
public class FileStorageController {

  private final FileStorageService fileService;

  @PostMapping(value = "/avatars", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @Operation(summary = "Upload a single file", description = "Upload a single file to Cloudinary")
  public ResponseEntity<ApiResponseDto<FileUploadResponse>> uploadFile(
      @Parameter(description = "File to upload", required = true) @RequestParam("file") MultipartFile file,

      @Parameter(description = "Folder to store file in", example = "avatars") @RequestParam(value = "folder", defaultValue = "avatars") String folder) {

    FileUploadResponse response = fileService.uploadFile(file, folder);
    return ApiResponseUtil.success(
        "File uploaded successfully",
        response);
  }

  @PostMapping(value = "/upload-multiple", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @Operation(summary = "Upload multiple files", description = "Upload multiple files at once to the same folder")
  public ResponseEntity<ApiResponseDto<MultipleFileUploadResponse>> uploadMultipleFiles(
      @Parameter(description = "Files to upload", required = true) @RequestParam("files") List<MultipartFile> files,

      @Parameter(description = "Folder to store files in", example = "documents") @RequestParam(value = "folder", defaultValue = "uploads") String folder) {

    if (files.size() > 10) {
      throw new InvalidFileException("Cannot upload more than 10 files at once");
    }

    List<FileUploadResponse> uploadedFiles = fileService.uploadMultipleFiles(files, folder);

    MultipleFileUploadResponse response = MultipleFileUploadResponse.builder()
        .files(uploadedFiles)
        .totalUploaded(uploadedFiles.size())
        .build();

    return ApiResponseUtil.success(
        "Files uploaded successfully",
        response);
  }

  @DeleteMapping()
  @Operation(summary = "Delete a file", description = "Delete a file from Cloudinary using its file url")
  public ResponseEntity<ApiResponseDto<Void>> deleteFile(
      @Parameter(
        description = "URL of the file to delete",
        example = "https://res.cloudinary.com/dbvv8bryb/image/upload/v1750330030/avatars/file_b66xas.jpg",
        required = true)
    @RequestParam String fileUrl) {

     fileService.deleteFile(fileUrl);
     return ApiResponseUtil.success("Image deleted successfully");
     
  }

}
