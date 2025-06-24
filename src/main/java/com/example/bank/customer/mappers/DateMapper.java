package com.example.bank.customer.mappers;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DateMapper {

  default LocalDate map(String date) {
    return date != null ? LocalDate.parse(date) : null;
  }

  default String map(LocalDate date) {
    return date != null ? date.toString() : null;
  }

  default LocalDateTime map(LocalDateTime dateTime) {
    return dateTime;
  }
}