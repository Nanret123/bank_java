package com.example.bank.customer.dtos;


import com.example.bank.customer.enums.CustomerType;
import com.example.bank.customer.enums.MaritalStatus;
import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank; 
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Request object for updating customer details")
public class UpdateCustomerRequest {
    @Schema(description = "first name of the user", example = "John")
    private String firstName;

    @Schema(description = "middle name of the user", example = "James")
    private String middleName;

    @Schema(description = "last name of the user", example = "Doe")
    private String lastName;

    @Schema(description = "Title of the user", example = "Mr")
    private String title;

    @Schema(description = "Marital status of the user")
    private MaritalStatus maritalStatus;

    @Schema(description = "nationality of the user", example = "Nigeria")
    private String nationality;

    @Schema(description = "valid email of the user")
    @Email(message = "Please provide a valid email address")
    private String email;

    @Schema(description = "phone number of the user", example = "+2348123456789")
    @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Please provide a valid phone number")
    private String phoneNumber;

    @Schema(description = "alternative phone number of the user", example = "+2348123456789")
    @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Please provide a valid alternative phone number")
    private String alternativePhone;

    @Schema(description = "residential address of the user", example = "23 Street, Rayfield Road")
    private String residentialAddress;

    @Schema(description = "city of the user", example = "Jos")
    private String city;

    @Schema(description = "state of the user", example = "Plateau")
    private String state;

    @Schema(description = "country of the user", example = "Nigeria")
    private String country;

    @Schema(description = "occupation of the user", example = "Bricklayer")
    private String occupation;

    @Schema(description = "Customer type", example = "INDIVIDUAL")
    private CustomerType customerType;

    @Schema(description = "Emergency contact name", example = "Jane Doe")
    private String emergencyContactName;

    @Schema(description = "Relationship with emergency contact", example = "Sister")
    private String emergencyContactRelationship;

    @Schema(description = "Phone number of emergency contact", example = "+2347011122233")
    private String emergencyContactPhone;

    @Schema(description = "Branch code associated with the customer", example = "BR001")
    private String branchCode;
}
