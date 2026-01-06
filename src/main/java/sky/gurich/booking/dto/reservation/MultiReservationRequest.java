package sky.gurich.booking.dto.reservation;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import sky.gurich.booking.entity.ReservationType;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class MultiReservationRequest {

    @NotNull(message = "예약 대상 식별자 목록은 필수 값입니다.")
    @Size(min = 1, max = 10, message = "한 번에 최소 1개, 최대 10개까지 예약 가능합니다.")
    private List<Long> ids; // carIds 또는 roomIds

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

    private String destination; // 차량 예약 시

    @NotBlank(message = "용도는 필수 값입니다.")
    private String purpose;

    private Integer attendees; // 부속실 예약 시
}
