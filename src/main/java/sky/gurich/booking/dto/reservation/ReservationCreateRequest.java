package sky.gurich.booking.dto.reservation;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import sky.gurich.booking.entity.*;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReservationCreateRequest {

    @NotNull(message = "예약 대상 식별자는 필수 값입니다. (차량, 부속실 식별자)")
    private Long id;

    @NotNull(message = "예약자 식별자는 필수 값입니다.")
    private Long userId;

    @NotNull(message = "예약 타입은 필수 값입니다.")
    private ReservationType reservationType;

    @NotNull(message = "예약 시작 시간은 필수 값입니다.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @FutureOrPresent(message = "예약 시작 시간은 과거일 수 없습니다.")
    private LocalDateTime reservationStartAt;

    @NotNull(message = "예약 종료 시간은 필수 값입니다.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @FutureOrPresent(message = "예약 종료 시간은 과거일 수 없습니다.")
    private LocalDateTime reservationEndAt;

    private String destination; // 차량 예약 시 필수

    @NotBlank(message = "용도는 필수 값입니다.")
    private String purpose; // 공통 필수

    private Integer attendees; // 부속실 예약 시 필수

    public Reservation toEntity(Member member, Car car, Room room) {
        return Reservation.builder()
                .reservationType(this.reservationType)
                .member(member)
                .car(car)
                .room(room)
                .reservationStartAt(this.reservationStartAt)
                .reservationEndAt(this.reservationEndAt)
                .destination(this.destination)
                .purpose(this.purpose)
                .attendees(this.attendees)
                .build();
    }
}
