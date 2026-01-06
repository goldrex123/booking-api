# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 프로젝트 개요
교회 시설(차량, 방) 예약 시스템을 위한 Spring Boot REST API 애플리케이션

## 기술 스택
- **언어**: Java 17
- **프레임워크**: Spring Boot 3.5.0
- **데이터베이스**: H2 (local), JPA/Hibernate
- **보안**: Spring Security + JWT (jjwt 0.12.3)
- **캐시**: Redis (refresh token 저장용)
- **빌드 도구**: Gradle
- **기타**: Lombok, Bean Validation

## 필수 명령어

### 애플리케이션 실행
```bash
# local 프로필로 실행 (기본값)
./gradlew bootRun

# 특정 프로필 지정
./gradlew bootRun --args='--spring.profiles.active=prod'
```

### 빌드
```bash
# 빌드 (테스트 포함)
./gradlew build

# 테스트 없이 빌드
./gradlew build -x test
```

### 테스트
```bash
# 모든 테스트 실행
./gradlew test

# 특정 테스트 클래스 실행
./gradlew test --tests BookingApplicationTests
```

### 기타
```bash
# 의존성 확인
./gradlew dependencies

# 프로젝트 정리
./gradlew clean
```

## 아키텍처 및 패키지 구조

```
src/main/java/sky/gurich/booking/
├── common/          # 공통 응답 클래스 (ApiResponse, ApiResponseCode)
├── config/          # Spring 설정 (SecurityConfig)
├── controller/      # REST API 엔드포인트 (AuthController, CarController, RoomController, ReservationController)
├── dto/             # 요청/응답 DTO (auth, car, room, reservation)
├── entity/          # JPA 엔티티 (Member, Car, Room, Reservation, BaseTimeEntity)
├── exception/       # 커스텀 예외 (ExpiredTokenException)
├── handler/         # 전역 예외 핸들러 (BizExceptionHandler)
├── jwt/             # JWT 관련 (JWTUtil, JwtLoginFilter, JwtAuthorizationFilter)
├── repository/      # Spring Data JPA 레포지토리
└── service/         # 비즈니스 로직 (AuthService, CarService, RoomService, ReservationService, CustomUserDetailsService, RefreshTokenService)
```

## 핵심 아키텍처 패턴

### 1. JWT 기반 인증/인가
- **Access Token**: 사용자 인증용 (기본 24시간)
- **Refresh Token**: Access Token 갱신용 (Redis에 저장)
- **필터 체인**: `JwtAuthorizationFilter` → Controller
- **공개 엔드포인트**: `/api/auth/**` (회원가입, 로그인)
- **보호 엔드포인트**: 나머지 모든 API (JWT 필수)

### 2. 통일된 API 응답 구조
모든 API는 `ApiResponse<T>` 형식으로 응답:
```java
{
  "code": "SUCCESS",        // ApiResponseCode enum
  "message": "성공",
  "data": { ... }           // 실제 응답 데이터
}
```

### 3. 전역 예외 처리
`BizExceptionHandler`에서 모든 예외를 일관되게 처리:
- `MethodArgumentNotValidException` → Validation 실패 (400)
- `EntityNotFoundException` → 엔티티 없음 (404)
- `BadCredentialsException` → 로그인 실패 (401)
- `JwtException` → JWT 오류 (401)
- `IllegalStateException/IllegalArgumentException` → 비즈니스 로직 오류 (400)

### 4. 메시지 국제화
`errors.properties`에서 validation 오류 메시지 관리:
- `typeMismatch.*` 형식으로 필드별 타입 오류 메시지 정의
- `MessageSource`를 통해 동적 메시지 로딩

### 5. JPA Auditing
`BaseTimeEntity`를 상속받아 생성/수정 시간 자동 관리:
- `@EnableJpaAuditing(modifyOnCreate = false)` 설정
- 모든 엔티티는 `createdAt`, `lastModifiedAt` 자동 추적

### 6. 예약 시스템 구조
- **Reservation**: 차량(Car) 또는 방(Room) 예약을 단일 엔티티로 관리
- **ReservationType**: `CAR`, `ROOM` enum으로 구분
- **연관관계**: Reservation ↔ Member (예약자), Reservation ↔ Car/Room (예약 대상)

## 개발 시 주의사항

### 1. 엔티티 설계
- Lombok의 `@NoArgsConstructor(access = AccessLevel.PROTECTED)` 사용
- `@Builder` 패턴으로 객체 생성
- 연관관계는 `FetchType.LAZY` 기본 사용
- `BaseTimeEntity` 상속 필수

### 2. DTO Validation
- 요청 DTO에 `@Validated` 사용
- Bean Validation 애노테이션 활용 (`@NotNull`, `@NotBlank` 등)
- enum 타입 불일치 시 친절한 에러 메시지 제공 (허용값 표시)

### 3. 보안
- JWT secret은 환경별 설정 파일에서 관리 (`application-{profile}.yml`)
- CORS 설정: 현재 모든 origin 허용 중 (프로덕션에서 수정 필요)
- 세션 사용 안 함 (`SessionCreationPolicy.STATELESS`)

### 4. 예외 처리
- 비즈니스 로직 오류는 `IllegalStateException` 또는 `IllegalArgumentException` 사용
- 예외 메시지는 한글로 작성
- 커스텀 예외는 `exception` 패키지에 정의

### 5. Redis
- Refresh token 저장/조회용으로만 사용
- `RefreshTokenService`에서 관리
- local 환경: `localhost:6379`

### 6. 프로필 관리
- **local**: H2 데이터베이스, DDL auto-create, SQL 로깅 활성화
- **prod**: 프로덕션 설정 (별도 데이터베이스 설정 필요)
- 기본 활성 프로필: `local`

## API 엔드포인트 구조
- `/api/auth/**` - 인증 (회원가입, 로그인, 토큰 갱신)
- `/api/car/**` - 차량 관리
- `/api/room/**` - 방 관리
- `/api/reservation/**` - 예약 조회/생성/삭제

## 로깅
- SLF4J + Logback 사용
- 모든 예외는 `BizExceptionHandler`에서 로그 출력
- JWT 검증 오류는 `JWTUtil`에서 로그 출력
