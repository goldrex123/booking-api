package sky.gurich.booking.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ApiResponseCode {
    SUCCESS("SUCCESS", "요청 성공"),
    VALIDATION_FAIL("VALIDATION_FAIL", "잘못된 요청입니다"),
    NOT_FOUND("NOT_FOUND", "잘못된 API 경로 요청입니다");

    private final String code;
    private final String message;
}
