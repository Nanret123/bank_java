package com.example.bank.common.dto;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Data Transfer Object for Paginated Requests")
public class PaginationRequest {

    @Parameter(description = "Page number", example = "0")
    private int page = 0;

    @Parameter(description = "Items per page", example = "10")
    private int size = 10;

    @Parameter(description = "Field to sort by", example = "id")
    private String sortBy = "id";

    @Parameter(description = "Sort direction (asc or desc)", example = "asc")
    private String sortDirection = "asc";
}