package sky.gurich.booking.dto.reservation;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import sky.gurich.booking.entity.ReservationType;

import java.time.LocalDateTime;

@Getter
public class ReservationSearchRequest {

    @NotNull(message = "예약 타입은 필수 값입니다.")
    private ReservationType reservationType;

    private LocalDateTime reservationStartAt;
    private LocalDateTime reservationEndAt;
}
