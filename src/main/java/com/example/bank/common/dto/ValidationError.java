package com.example.bank.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidationError {
    private String field;
    private String message;
    private Object rejectedValue;

    public static ValidationError of(String field, Object rejectedValue, String message, String code) {
        return ValidationError.builder()
                .field(field)
                .rejectedValue(rejectedValue)
                .message(message)
                .build();
    }

}
