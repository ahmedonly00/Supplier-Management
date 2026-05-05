package Supplier.Mgt.Supplier.Mgt.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Standard API response envelope used by every endpoint")
public class ApiResponse<T> {

    @Schema(description = "true when the operation succeeded, false on error")
    private boolean success;

    @Schema(description = "Human-readable result message")
    private String message;

    @Schema(description = "Response payload — absent on error")
    private T data;

    @Schema(description = "Server timestamp of the response")
    private LocalDateTime timestamp;

    // ── factory helpers ───────────────────────────────────────────────────────
    // Use setter-based construction to avoid Lombok generic builder type-witness
    // limitation (ApiResponse.<T>builder() does not compile with javac).

    public static <T> ApiResponse<T> success(String message, T data) {
        ApiResponse<T> r = new ApiResponse<>();
        r.setSuccess(true);
        r.setMessage(message);
        r.setData(data);
        r.setTimestamp(LocalDateTime.now());
        return r;
    }

    public static <T> ApiResponse<T> failure(String message, T errors) {
        ApiResponse<T> r = new ApiResponse<>();
        r.setSuccess(false);
        r.setMessage(message);
        r.setData(errors);
        r.setTimestamp(LocalDateTime.now());
        return r;
    }

    public static <T> ApiResponse<T> error(String message) {
        return failure(message, null);
    }
}
