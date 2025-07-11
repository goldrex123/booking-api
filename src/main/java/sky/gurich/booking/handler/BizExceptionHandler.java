package sky.gurich.booking.handler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import sky.gurich.booking.common.ApiResponse;
import sky.gurich.booking.common.ApiResponseCode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class BizExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("handle MethodArgumentNotValidException - {}", ex.getMessage());

        BindingResult bindingResult = ex.getBindingResult();
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : bindingResult.getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        ApiResponse<Map<String, String>> response = ApiResponse.fail(ApiResponseCode.VALIDATION_FAIL, errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.error("handle HttpMessageNotReadableException - {}", ex.getMessage());

        Throwable cause = ex.getCause();
        String message = "잘못된 Request Body 데이터 입니다";

        if (cause instanceof InvalidFormatException invalidFormatException) {
            Class<?> targetType = invalidFormatException.getTargetType();
            String fieldName = invalidFormatException.getPath().stream()
                    .map(ref -> ref.getFieldName())
                    .reduce((prev, curr) -> curr)
                    .orElse("알 수 없는 필드");

            message = String.format("%s 필드는 %s 타입이어야 합니다.", fieldName, targetType.getSimpleName());

            if (targetType.isEnum()) {
                Object[] enumConstants = targetType.getEnumConstants();
                String allowed = Arrays.stream(enumConstants)
                        .map(o -> o.toString())
                        .collect(Collectors.joining(","));

                message += " 허용 값: [" + allowed + "]";
            }
        }

        ApiResponse<String> response = ApiResponse.fail(ApiResponseCode.REQUEST_BODY_NOT_READABLE, message);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        log.error("handle HttpRequestMethodNotSupportedException - {}", ex.getMessage());

        ApiResponse<String> response = ApiResponse.fail(ApiResponseCode.REQUEST_METHOD_NOT_SUPPORTED, ex.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponse> handleEntityNotFoundException(EntityNotFoundException ex) {
        log.error("handle EntityNotFoundException - {}", ex.getMessage());

        ApiResponse<String> response = ApiResponse.fail(ApiResponseCode.ENTITY_NOT_FOUND, ex.getMessage());
        return ResponseEntity.status(404).body(response);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        log.error("handle MethodArgumentTypeMismatchException - {}", ex.getMessage());

        String name = ex.getName();
        String requiredType = ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "알 수 없음";

        String message = String.format("%s 파라미터는 %s 타입이어야 합니다.", name, requiredType);

        ApiResponse<String> response = ApiResponse.fail(ApiResponseCode.TYPE_MISMATCH, message);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        log.error("handle DataIntegrityViolationException - {}", ex.getMessage());

        ApiResponse<String> response = ApiResponse.fail(ApiResponseCode.DATA_INTEGRITY_VIOLATION, ex.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResponse> handleIllegalStateException(IllegalStateException ex) {
        log.error("handle IllegalStateException - {}", ex.getMessage());

        ApiResponse<String> response = ApiResponse.fail(ApiResponseCode.ILLEGAL_STATE, ex.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse> handleBadCredentialsException(BadCredentialsException ex) {
        log.error("handle BadCredentialsException - {}", ex.getMessage());

        ApiResponse<String> response = ApiResponse.fail(ApiResponseCode.LOGIN_FAIL, ex.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleMethodArgumentNotValidException(Exception ex) {
        log.error("handle Exception - {}", ex.getMessage(), ex);

        ApiResponse<String> response = ApiResponse.fail(ApiResponseCode.SERVER_SIDE_EXCEPTION, ex.getMessage());
        return ResponseEntity.status(500).body(response);
    }

}
