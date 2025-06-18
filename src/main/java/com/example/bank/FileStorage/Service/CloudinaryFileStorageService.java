package com.example.bank.FileStorage.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Service
public class CloudinaryFileStorageService implements IFileStorage {

  private final Cloudinary cloudinary;

  public CloudinaryFileStorageService(Cloudinary cloudinary) {
    this.cloudinary = cloudinary;
  }

  @Override
  public String storeFile(MultipartFile file,  String folderPath) {
    try {
      // String folderPath = "uploads/" + LocalDate.now();

      Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
          "folder", folderPath));
      return uploadResult.get("secure_url").toString(); // This is the public URL
    } catch (IOException e) {
      throw new RuntimeException("Cloudinary upload failed", e);
    }
  }

  @Override
  public void deleteFile(String fileUrl) {
    try {
      // Cloudinary deletes based on public_id, not full URL
      String publicId = extractPublicId(fileUrl);
      cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    } catch (Exception e) {
      throw new RuntimeException("Cloudinary delete failed", e);
    }
  }

  private String extractPublicId(String fileUrl) {
    // Example: extract "profile_pics/abc123" from
    // https://res.cloudinary.com/demo/image/upload/v123456/profile_pics/abc123.jpg
    int start = fileUrl.indexOf("/upload/") + 8;
    String partial = fileUrl.substring(start);
    return partial.substring(0, partial.lastIndexOf('.')); // Remove extension
  }

}
