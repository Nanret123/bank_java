package com.example.bank.common.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.example.bank.common.dto.PaginationRequest;

public class BuildPageable {
  public static Pageable createPageable(PaginationRequest filter) {
    Sort sort = createSort(filter.getSortBy(), filter.getSortDirection());
    return PageRequest.of(filter.getPage(), filter.getSize(), sort);
  }

  private static Sort createSort(String sortBy, String sortDirection) {
    Sort.Direction direction = "desc".equalsIgnoreCase(sortDirection)
        ? Sort.Direction.DESC
        : Sort.Direction.ASC;

    return Sort.by(direction, sortBy);
  }
}
