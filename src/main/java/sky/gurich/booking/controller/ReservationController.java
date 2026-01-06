package sky.gurich.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sky.gurich.booking.common.ApiResponse;
import sky.gurich.booking.common.ApiResponseCode;
import sky.gurich.booking.dto.reservation.*;
import sky.gurich.booking.entity.ReservationType;
import sky.gurich.booking.service.ReservationService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservation")
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping
    public ResponseEntity<?> getReserve(@Validated @ModelAttribute ReservationSearchRequest request) {
        List<ReservationResponse> response = reservationService.findReserve(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }


    @PostMapping
    public ResponseEntity<?> reserve(@RequestBody @Validated ReservationCreateRequest request) {
        ReservationResponse response = reservationService.reserve(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelReserve(@PathVariable Long id) {
        reservationService.cancelReserve(id);
        return ResponseEntity.ok(ApiResponse.success("예약이 성공적으로 삭제되었습니다."));
    }

    /**
     * 예약 수정
     * PUT /api/reservation/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateReservation(
            @PathVariable Long id,
            @RequestBody @Validated ReservationUpdateRequest request) {
        ReservationResponse response = reservationService.updateReservation(id, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 다중 차량 예약
     * POST /api/reservation/multi/vehicle
     */
    @PostMapping("/multi/vehicle")
    public ResponseEntity<?> multiVehicleReservation(
            @RequestBody @Validated MultiReservationRequest request) {

        if (request.getReservationType() != ReservationType.CAR) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.fail(ApiResponseCode.ILLEGAL_ARGUMENT,
                          "차량 예약 타입이어야 합니다."));
        }

        MultiReservationResponse response = reservationService.multiReserve(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 다중 부속실 예약
     * POST /api/reservation/multi/room
     */
    @PostMapping("/multi/room")
    public ResponseEntity<?> multiRoomReservation(
            @RequestBody @Validated MultiReservationRequest request) {

        if (request.getReservationType() != ReservationType.ROOM) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.fail(ApiResponseCode.ILLEGAL_ARGUMENT,
                          "부속실 예약 타입이어야 합니다."));
        }

        MultiReservationResponse response = reservationService.multiReserve(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 사용자별 예약 조회
     * GET /api/reservation/user/{userId}
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserReservations(@PathVariable Long userId) {
        List<ReservationResponse> response = reservationService.findByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 날짜 범위 예약 조회 (전체)
     * GET /api/reservation/date-range?start={start}&end={end}&type={type}
     */
    @GetMapping("/date-range")
    public ResponseEntity<?> getReservationsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam(required = false) ReservationType type) {

        List<ReservationResponse> response =
                reservationService.findByDateRange(start, end, type);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
