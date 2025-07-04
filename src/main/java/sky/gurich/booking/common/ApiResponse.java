package sky.gurich.booking.common;

import lombok.Getter;

@Getter
public class ApiResponse<T> {

    private ApiResponseCode code;
    private String message;
    private T data;


    public ApiResponse(ApiResponseCode code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(ApiResponseCode.SUCCESS, ApiResponseCode.SUCCESS.getMessage(), data);
    }

    public static <T> ApiResponse<T> fail(ApiResponseCode apiResponseCode, T data) {
        return new ApiResponse<>(apiResponseCode, apiResponseCode.getMessage(), data);
    }
}
