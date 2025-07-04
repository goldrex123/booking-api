package sky.gurich.booking.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ApiResponseCode {
    SUCCESS("SUCCESS", "요청 성공"),
    VALIDATION_FAIL("VALIDATION_FAIL", "잘못된 요청입니다"),
    SERVER_SIDE_EXCEPTION("SERVER_SIDE_EXCEPTION", "서버 측 오류 입니다"),
    REQUEST_BODY_NOT_READABLE("REQUEST_BODY_NOT_READABLE", "잘못된 Request Body 데이터 입니다"),
    REQUEST_METHOD_NOT_SUPPORTED("REQUEST_METHOD_NOT_SUPPORTED", "잘못된 요청 메소드 입니다."),
    NOT_FOUND("NOT_FOUND", "잘못된 API 경로 요청입니다");

    private final String code;
    private final String message;
}
