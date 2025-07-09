package sky.gurich.booking.dto;

import lombok.Builder;
import lombok.Getter;
import sky.gurich.booking.entity.Reservation;

import java.time.LocalDateTime;

@Getter
public class ReservationResponse {

    private Long id;

    private Long userId;
    private String username;

    private CarResponse carResponse;
    private RoomResponse roomResponse;

    private LocalDateTime reservationStartAt;
    private LocalDateTime reservationEndAt;


    @Builder
    public ReservationResponse(Long id, Long userId, String username, CarResponse carResponse, RoomResponse roomResponse, LocalDateTime reservationStartAt, LocalDateTime reservationEndAt) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.carResponse = carResponse;
        this.roomResponse = roomResponse;
        this.reservationStartAt = reservationStartAt;
        this.reservationEndAt = reservationEndAt;
    }

    public static ReservationResponse toDto(Reservation reservation) {
        return ReservationResponse.builder()
                .id(reservation.getId())
                .userId(reservation.getUser().getId())
                .username(reservation.getUser().getUsername())
                .carResponse(CarResponse.toDto(reservation.getCar()))
                .roomResponse(RoomResponse.toDto(reservation.getRoom()))
                .reservationStartAt(reservation.getReservationStartAt())
                .reservationEndAt(reservation.getReservationEndAt())
                .build();
    }
}
