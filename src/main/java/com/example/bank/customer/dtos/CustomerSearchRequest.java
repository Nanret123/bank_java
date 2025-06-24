package com.example.bank.customer.dtos;

import com.example.bank.common.dto.PaginationRequest;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request object for searching customers. Contains pagination, sorting, and search criteria.")
public class CustomerSearchRequest extends PaginationRequest {

    @Parameter(description = "Search text to find in customer name, email, or phone number", example = "john@example.com")
    private String searchText;
}
