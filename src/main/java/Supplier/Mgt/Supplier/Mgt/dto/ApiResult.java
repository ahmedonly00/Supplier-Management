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
@Schema(description = "Standard API response envelope")
public class ApiResult<T> {

    private boolean success;
    private String message;
    private T data;
    private LocalDateTime timestamp;

    public static <T> ApiResult<T> success(String message, T data) {
        ApiResult<T> r = new ApiResult<>();
        r.setSuccess(true);
        r.setMessage(message);
        r.setData(data);
        r.setTimestamp(LocalDateTime.now());
        return r;
    }

    public static <T> ApiResult<T> failure(String message, T errors) {
        ApiResult<T> r = new ApiResult<>();
        r.setSuccess(false);
        r.setMessage(message);
        r.setData(errors);
        r.setTimestamp(LocalDateTime.now());
        return r;
    }

    public static <T> ApiResult<T> error(String message) {
        return failure(message, null);
    }
}
