package sky.gurich.booking.dto.reservation;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import sky.gurich.booking.dto.car.CarResponse;
import sky.gurich.booking.dto.room.RoomResponse;
import sky.gurich.booking.entity.Reservation;
import sky.gurich.booking.entity.ReservationType;

import java.time.LocalDateTime;

@Getter
public class ReservationResponse {

    private Long id;

    private Long userId;
    private String username;

    private CarResponse carResponse;
    private RoomResponse roomResponse;

    private ReservationType reservationType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime reservationStartAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime reservationEndAt;

    private String destination;
    private String purpose;
    private Integer attendees;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime lastModifiedAt;

    @Builder
    public ReservationResponse(Long id, Long userId, String username, CarResponse carResponse, RoomResponse roomResponse,
                              ReservationType reservationType, LocalDateTime reservationStartAt, LocalDateTime reservationEndAt,
                              String destination, String purpose, Integer attendees,
                              LocalDateTime createdAt, LocalDateTime lastModifiedAt) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.carResponse = carResponse;
        this.roomResponse = roomResponse;
        this.reservationType = reservationType;
        this.reservationStartAt = reservationStartAt;
        this.reservationEndAt = reservationEndAt;
        this.destination = destination;
        this.purpose = purpose;
        this.attendees = attendees;
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
    }

    public static ReservationResponse toDto(Reservation reservation) {
        return ReservationResponse.builder()
                .id(reservation.getId())
                .userId(reservation.getMember().getId())
                .username(reservation.getMember().getUsername())
                .carResponse(reservation.getCar() == null ? null : CarResponse.toDto(reservation.getCar()))
                .roomResponse(reservation.getRoom() == null ? null : RoomResponse.toDto(reservation.getRoom()))
                .reservationType(reservation.getReservationType())
                .reservationStartAt(reservation.getReservationStartAt())
                .reservationEndAt(reservation.getReservationEndAt())
                .destination(reservation.getDestination())
                .purpose(reservation.getPurpose())
                .attendees(reservation.getAttendees())
                .createdAt(reservation.getCreatedAt())
                .lastModifiedAt(reservation.getLastModifiedAt())
                .build();
    }
}
