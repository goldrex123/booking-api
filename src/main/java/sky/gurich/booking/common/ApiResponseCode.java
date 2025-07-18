package sky.gurich.booking.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ApiResponseCode {
    //-- 정상 요첨 --//
    SUCCESS("SUCCESS", "요청 성공"),

    //-- 요청 실패 --//
    LOGIN_FAIL("LOGIN_FAIL", "로그인이 실패했습니다."),
    AUTHENTICATION_FAIL("AUTHENTICATION_FAIL", "인증 실패에 실패했습니다."),
    ACCESS_FAIL("ACCESS_FAIL", "권한이 없는 요청입니다."),
    VALIDATION_FAIL("VALIDATION_FAIL", "잘못된 요청입니다"),
    EXPIRED_REFRESH_TOKEN("EXPIRED_REFRESH_TOKEN", "만료된 Refresh 토큰 입니다."),
    SERVER_SIDE_EXCEPTION("SERVER_SIDE_EXCEPTION", "서버 측 오류 입니다"),
    REQUEST_BODY_NOT_READABLE("REQUEST_BODY_NOT_READABLE", "잘못된 Request Body 데이터 입니다"),
    REQUEST_METHOD_NOT_SUPPORTED("REQUEST_METHOD_NOT_SUPPORTED", "잘못된 요청 메소드 입니다."),
    ENTITY_NOT_FOUND("ENTITY_NOT_FOUND", "요청하신 데이터가 없습니다."),
    TYPE_MISMATCH("TYPE_MISMATCH", "잘못된 요청 파라미터 타입입니다."),
    DATA_INTEGRITY_VIOLATION("DATA_INTEGRITY_VIOLATION", "데이터 무결성 오류 입니다."),
    ILLEGAL_STATE("ILLEGAL_STATE", "요청을 수행할 수 없는 상태입니다."),
    ILLEGAL_ARGUMENT("ILLEGAL_ARGUMENT", "잘못된 요청 인자입니다."),
    NOT_FOUND("NOT_FOUND", "잘못된 API 경로 요청입니다");

    private final String code;
    private final String message;
}
