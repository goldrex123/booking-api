package sky.gurich.booking.dto.reservation;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import sky.gurich.booking.entity.*;

import java.time.LocalDateTime;

@Getter
public class ReservationCreateRequest {

    @NotNull(message = "예약 대상 식별자는 필수 값입니다. (차량, 부속실 식별자)")
    private Long id;

    @NotNull(message = "예약자 식별자는 필수 값입니다.")
    private Long userId;

    @NotNull(message = "예약 타입은 필수 값입니다.")
    private ReservationType reservationType;

    @NotNull(message = "예약 시작 시간은 필수 값입니다.")
    private LocalDateTime reservationStartAt;
    @NotNull(message = "예약 종료 시간은 필수 값입니다.")
    private LocalDateTime reservationEndAt;

    public Reservation toEntity(Member member, Car car, Room room) {
        return Reservation.builder()
                .reservationType(this.reservationType)
                .member(member)
                .car(car)
                .room(room)
                .reservationStartAt(this.reservationStartAt)
                .reservationEndAt(this.reservationEndAt)
                .build();
    }
}
