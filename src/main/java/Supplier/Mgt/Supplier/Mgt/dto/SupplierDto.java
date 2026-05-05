package Supplier.Mgt.Supplier.Mgt.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class SupplierDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "SupplierRequest", description = "Payload for creating or updating a supplier")
    public static class Request {

        @NotBlank(message = "Name is required")
        @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
        @Schema(description = "Full name of the supplier", example = "Acme Corp")
        private String name;

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        @Schema(description = "Contact email address", example = "contact@acme.com")
        private String email;

        @NotBlank(message = "Phone is required")
        @Pattern(regexp = "^[+]?[0-9\\s\\-()]{7,20}$", message = "Phone number is invalid")
        @Schema(description = "Contact phone number", example = "+1-800-555-0100")
        private String phone;

        @Schema(description = "Physical address of the supplier", example = "123 Main St, Springfield")
        private String address;

        @Schema(description = "Primary contact person at the supplier", example = "Jane Doe")
        private String contactPerson;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "SupplierResponse", description = "Supplier details returned by the API")
    public static class Response {

        @Schema(description = "Unique identifier", example = "1")
        private Long id;

        @Schema(description = "Supplier name", example = "Acme Corp")
        private String name;

        @Schema(description = "Contact email", example = "contact@acme.com")
        private String email;

        @Schema(description = "Contact phone", example = "+1-800-555-0100")
        private String phone;

        @Schema(description = "Physical address")
        private String address;

        @Schema(description = "Primary contact person")
        private String contactPerson;

        @Schema(description = "Current status", example = "ACTIVE")
        private String status;

        @Schema(description = "Record creation timestamp")
        private LocalDateTime createdAt;

        @Schema(description = "Last update timestamp")
        private LocalDateTime updatedAt;
    }
}
