package com.example.bank.FileStorage.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.bank.FileStorage.dto.FileUploadResponse;
import com.example.bank.common.exception.InvalidFileException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FileStorageService {

  private final Cloudinary cloudinary;

  public FileStorageService(Cloudinary cloudinary) {
    this.cloudinary = cloudinary;
  }

  public FileUploadResponse uploadFile(MultipartFile file,  String folderPath) {
    try {
      validateFile(file); // Validate the file before uploading

      Map<String, Object> uploadParams = new HashMap<>();
      uploadParams.put("folder", folderPath); 
      uploadParams.put("resource_type", "image");
      uploadParams.put("use_filename", true); 
      uploadParams.put("unique_filename", true); 

      Map<String, Object> result = cloudinary.uploader().upload(file.getBytes(), uploadParams);

      //build response
      return FileUploadResponse.builder()
          .publicId((String) result.get("public_id"))
          .url((String) result.get("secure_url"))
          .originalFileName(file.getOriginalFilename())
          .size(file.getSize())
          .contentType(file.getContentType())
          .uploadedAt(LocalDateTime.now())
          .build();
      
    } catch (IOException e) {
      throw new InvalidFileException("Cloudinary upload failed: " + e.getMessage());
    }
  }

  public List<FileUploadResponse> uploadMultipleFiles(List<MultipartFile> files, String folder) {
        return files.stream()
                .map(file -> uploadFile(file, folder))
                .collect(Collectors.toList());
    }


    public boolean deleteFile(String publicId) {
        try {
            Map<String, Object> result = cloudinary.uploader().destroy(publicId, Map.of());
            return "ok".equals(result.get("result"));
        } catch (IOException e) {
            log.error("Error deleting file from Cloudinary: {}", e.getMessage());
            return false;
        }
    }

  public void validateFile(MultipartFile file) {
    if (file.isEmpty()) {
      throw new InvalidFileException("File cannot be empty");
    }
    if (file.getSize() > 10 * 1024 * 1024) { // 10MB limit
      throw new InvalidFileException("File size exceeds the maximum limit of 10MB");
    }
    String contentType = file.getContentType();
    if (contentType == null || !contentType.startsWith("image/")) {
      throw new InvalidFileException("Invalid file type. Only image files are allowed.");
    }

    List<String> allowedypes = Arrays.asList("image/jpeg", "image/png", "image/gif", "image/jpg");
    if (!allowedypes.contains(contentType)) {
      throw new InvalidFileException("Unsupported image format ");
    }
  }

}
