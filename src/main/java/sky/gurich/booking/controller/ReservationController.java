package sky.gurich.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sky.gurich.booking.common.ApiResponse;
import sky.gurich.booking.dto.reservation.ReservationCreateRequest;
import sky.gurich.booking.dto.reservation.ReservationResponse;
import sky.gurich.booking.dto.reservation.ReservationSearchRequest;
import sky.gurich.booking.service.ReservationService;

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
}
