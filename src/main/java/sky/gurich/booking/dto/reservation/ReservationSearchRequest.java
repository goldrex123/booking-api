package sky.gurich.booking.dto.reservation;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;
import sky.gurich.booking.entity.ReservationType;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ReservationSearchRequest {

    @NotNull(message = "예약 타입은 필수 값입니다.")
    private ReservationType reservationType;

    @NotNull(message = "조회 시작 시간은 필수 값입니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime searchStartAt;

    @NotNull(message = "조회 종료 시간은 필수 값입니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime searchEndAt;

    @NotNull(message = "예약 대상 식별자는 필수 값입니다. (차량, 부속실 식별자)")
    private Long id;
}