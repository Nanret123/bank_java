package com.example.bank.FileStorage.exceptions;

public class FileUploadException extends RuntimeException {
  public FileUploadException(String message){
    super(message);
  }

   public FileUploadException(String message, Throwable cause) {
    super(message, cause);
  }
}
