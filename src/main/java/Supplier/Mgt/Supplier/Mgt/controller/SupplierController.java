package Supplier.Mgt.Supplier.Mgt.controller;

import Supplier.Mgt.Supplier.Mgt.annotation.ApiConflictError;
import Supplier.Mgt.Supplier.Mgt.annotation.ApiNotFoundError;
import Supplier.Mgt.Supplier.Mgt.annotation.ApiValidationError;
import Supplier.Mgt.Supplier.Mgt.dto.ApiResult;
import Supplier.Mgt.Supplier.Mgt.dto.SupplierRequest;
import Supplier.Mgt.Supplier.Mgt.dto.SupplierResponse;
import Supplier.Mgt.Supplier.Mgt.service.SupplierService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Validated
@RestController
@RequestMapping("/api/v1/suppliers")
@RequiredArgsConstructor
@Tag(name = "Supplier Management")
public class SupplierController {

    private final SupplierService supplierService;

    @PostMapping("/add")
    @Operation(summary = "Create a new supplier")
    @ApiResponse(responseCode = "201", description = "Supplier created")
    @ApiValidationError
    @ApiConflictError
    public ResponseEntity<ApiResult<SupplierResponse>> addSupplier(
            @Valid @RequestBody SupplierRequest request) {

        SupplierResponse created = supplierService.addSupplier(request);

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/suppliers/{id}")
                .buildAndExpand(created.getId())
                .toUri();

        return ResponseEntity.created(location)
                .body(ApiResult.success("Supplier created successfully", created));
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Update a supplier")
    @ApiResponse(responseCode = "200", description = "Supplier updated")
    @ApiValidationError
    @ApiNotFoundError
    @ApiConflictError
    public ResponseEntity<ApiResult<SupplierResponse>> updateSupplier(
            @PathVariable @Positive Long id,
            @Valid @RequestBody SupplierRequest request) {

        return ResponseEntity.ok(
                ApiResult.success("Supplier updated successfully",
                        supplierService.updateSupplier(id, request)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a supplier by ID")
    @ApiResponse(responseCode = "200", description = "Supplier found")
    @ApiNotFoundError
    public ResponseEntity<ApiResult<SupplierResponse>> getSupplier(
            @PathVariable @Positive Long id) {

        return ResponseEntity.ok(
                ApiResult.success("Supplier retrieved successfully",
                        supplierService.getSupplier(id)));
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete a supplier")
    @ApiResponse(responseCode = "204", description = "Supplier deleted")
    @ApiNotFoundError
    public ResponseEntity<Void> deleteSupplier(
            @PathVariable @Positive Long id) {

        supplierService.deleteSupplier(id);
        return ResponseEntity.noContent().build();
    }
}
