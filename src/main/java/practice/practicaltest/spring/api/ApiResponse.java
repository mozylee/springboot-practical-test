package practice.practicaltest.spring.api;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
@NoArgsConstructor
public class ApiResponse<T> {

    private int code;

    private HttpStatus status;

    private String message;

    private T data;

    @Builder
    private ApiResponse(int code, HttpStatus status, String message, T data) {
        this.code = code;
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> of(HttpStatus status, T data) {
        return ApiResponse.<T>builder()
                          .code(status.value())
                          .status(status)
                          .message(status.name())
                          .data(data)
                          .build();
    }

    public static <T> ApiResponse<T> of(HttpStatus status, String message, T data) {
        return ApiResponse.<T>builder()
                          .code(status.value())
                          .status(status)
                          .message(message)
                          .data(data)
                          .build();
    }

    public static <T> ApiResponse<T> ok(T data) {
        return ApiResponse.of(HttpStatus.OK, data);
    }

    public static <T> ApiResponse<T> badRequest(String message, T data) {
        return ApiResponse.of(HttpStatus.BAD_REQUEST, message, data);
    }

}
