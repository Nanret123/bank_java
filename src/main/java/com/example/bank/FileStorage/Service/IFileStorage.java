package com.example.bank.FileStorage.Service;

import org.springframework.web.multipart.MultipartFile;

public interface IFileStorage {
  String storeFile(MultipartFile file,  String folderPath);
  void deleteFile(String filePath);
}
