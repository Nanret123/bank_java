package com.example.bank.FileStorage.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.bank.FileStorage.Service.CloudinaryFileStorageService;
import com.example.bank.common.dto.ApiResponseDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileStorageController {

  private final CloudinaryFileStorageService fileStorageService;

  @PostMapping("/upload")
  public ResponseEntity<ApiResponseDto<String>> uploadFile(@RequestParam("file") MultipartFile file,
      @RequestParam(defaultValue = "general") String folder) {
    String fileUrl = fileStorageService.storeFile(file, folder);
    ApiResponseDto<String> response = ApiResponseDto.<String>builder()
        .success(true)
        .message("File uploaded successfully")
        .data(fileUrl)
        .build();

    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/delete")
  public ResponseEntity<ApiResponseDto<Void>>  deleteFile(@RequestParam("fileUrl") String fileUrl) {
    fileStorageService.deleteFile(fileUrl);
    ApiResponseDto<Void> response = ApiResponseDto.<Void>builder()
        .success(true)
        .message("File deleted successfully")
        .data(null)
        .build();
    return ResponseEntity.ok(response);
  }

}
