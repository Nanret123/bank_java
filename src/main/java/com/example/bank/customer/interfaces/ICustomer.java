package com.example.bank.customer.interfaces;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;

import com.example.bank.customer.dtos.CreateCustomerRequest;
import com.example.bank.customer.dtos.CustomerFilter;
import com.example.bank.customer.dtos.CustomerResponse;
import com.example.bank.customer.dtos.CustomerSearchRequest;
import com.example.bank.customer.dtos.CustomerStatisticsResponse;
import com.example.bank.customer.dtos.CustomerStatusUpdateRequest;
import com.example.bank.customer.dtos.CustomerSummaryResponse;
import com.example.bank.customer.dtos.CustomerVerificationRequest;
import com.example.bank.customer.dtos.UpdateCustomerRequest;

public interface ICustomer {
    // Core CRUD operations
    CustomerResponse createCustomer(CreateCustomerRequest request, UUID userId);

    Optional<CustomerResponse> getCustomerById(UUID customerId);

    CustomerResponse updateCustomer(UUID customerId, UpdateCustomerRequest request, UUID userId);

    // CustomerResponse patchCustomer(Long customerId, CustomerPatchRequest
    // request);
    void deleteCustomer(UUID customerId, UUID userId);
    
    void restoreCustomer(UUID customerId, UUID userId); 
    
    void verifyCustomer(CustomerVerificationRequest request,  UUID userId);

    // // Search and listing
    Page<CustomerSummaryResponse> getAllCustomers(CustomerFilter filter);

    Page<CustomerSummaryResponse> searchCustomers(CustomerSearchRequest searchRequest);

      Page<CustomerSummaryResponse> getAllActiveCustomers(CustomerFilter filter);

    Page<CustomerSummaryResponse> searchActiveCustomers(CustomerSearchRequest searchRequest);
    // Page<CustomerSummaryResponse> advancedSearch(CustomerSearchRequest request);

    // // Status management
    CustomerResponse updateCustomerStatus(UUID customerId, CustomerStatusUpdateRequest request, UUID userId);

    // // Validation and verification
    //void validateCustomer(CustomerValidationRequest request);

    // // Reporting and analytics
     CustomerStatisticsResponse getCustomerStatistics();
    // CustomerActivityReportResponse getCustomerActivityReport(Long customerId,
    // LocalDate startDate, LocalDate endDate);

    // // Audit and history
    // Page<AuditTrailResponse> getCustomerAuditTrail(Long customerId, Pageable
    // pageable);
    // List<ChangeHistoryResponse> getCustomerChangeHistory(Long customerId);

    // // Bulk operations
    // BulkOperationResponse bulkCreateCustomers(List<CustomerCreateRequest>
    // requests);
    // BulkOperationResponse bulkUpdateCustomerStatus(BulkStatusUpdateRequest
    // request);

}
