package Supplier.Mgt.Supplier.Mgt.controller;

import Supplier.Mgt.Supplier.Mgt.annotation.ApiConflictError;
import Supplier.Mgt.Supplier.Mgt.annotation.ApiNotFoundError;
import Supplier.Mgt.Supplier.Mgt.annotation.ApiValidationError;
import Supplier.Mgt.Supplier.Mgt.dto.ApiResponse;
import Supplier.Mgt.Supplier.Mgt.dto.SupplierDto;
import Supplier.Mgt.Supplier.Mgt.service.SupplierService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

// NOTE: io.swagger.v3.oas.annotations.responses.ApiResponse is referenced by its
// fully-qualified name below to avoid collision with our ApiResponse<T> envelope.

@Validated
@RestController
@RequestMapping("/api/v1/suppliers")
@RequiredArgsConstructor
@Tag(name = "Supplier Management", description = "CRUD operations for managing suppliers")
public class SupplierController {

    private final SupplierService supplierService;

    @PostMapping("/add")
    @Operation(summary = "Add a new supplier",
            description = "Creates a new supplier and publishes a SUPPLIER_CREATED Kafka event")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201", description = "Supplier created successfully",
            content = @Content(schema = @Schema(implementation = SupplierDto.Response.class)))
    @ApiValidationError
    @ApiConflictError
    public ResponseEntity<ApiResponse<SupplierDto.Response>> addSupplier(
            @Valid @RequestBody SupplierDto.Request request) {

        SupplierDto.Response created = supplierService.addSupplier(request);

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/suppliers/{id}")
                .buildAndExpand(created.getId())
                .toUri();

        return ResponseEntity.created(location)
                .body(ApiResponse.success("Supplier created successfully", created));
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Update an existing supplier",
            description = "Updates supplier details and publishes a SUPPLIER_UPDATED Kafka event")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", description = "Supplier updated successfully",
            content = @Content(schema = @Schema(implementation = SupplierDto.Response.class)))
    @ApiValidationError
    @ApiNotFoundError
    @ApiConflictError
    public ResponseEntity<ApiResponse<SupplierDto.Response>> updateSupplier(
            @Parameter(description = "Supplier ID", required = true)
            @PathVariable @Positive(message = "Supplier ID must be a positive number") Long id,
            @Valid @RequestBody SupplierDto.Request request) {

        return ResponseEntity.ok(
                ApiResponse.success("Supplier updated successfully",
                        supplierService.updateSupplier(id, request)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get supplier by ID",
            description = "Retrieves a supplier and publishes a SUPPLIER_RETRIEVED Kafka event for audit")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", description = "Supplier found",
            content = @Content(schema = @Schema(implementation = SupplierDto.Response.class)))
    @ApiNotFoundError
    public ResponseEntity<ApiResponse<SupplierDto.Response>> getSupplier(
            @Parameter(description = "Supplier ID", required = true)
            @PathVariable @Positive(message = "Supplier ID must be a positive number") Long id) {

        return ResponseEntity.ok(
                ApiResponse.success("Supplier retrieved successfully",
                        supplierService.getSupplier(id)));
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete a supplier",
            description = "Permanently removes a supplier and publishes a SUPPLIER_DELETED Kafka event")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "204", description = "Supplier deleted successfully")
    @ApiNotFoundError
    public ResponseEntity<Void> deleteSupplier(
            @Parameter(description = "Supplier ID", required = true)
            @PathVariable @Positive(message = "Supplier ID must be a positive number") Long id) {

        supplierService.deleteSupplier(id);
        return ResponseEntity.noContent().build();
    }
}
