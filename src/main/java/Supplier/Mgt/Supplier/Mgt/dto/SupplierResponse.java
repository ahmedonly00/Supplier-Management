package Supplier.Mgt.Supplier.Mgt.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "SupplierResponse", description = "Supplier details returned by the API")
public class SupplierResponse {

    private Long id;

    private String name;

    private String email;

    private String phone;

    private String address;

    private String contactPerson;

    private String status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
