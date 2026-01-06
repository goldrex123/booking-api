package sky.gurich.booking.dto.reservation;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReservationUpdateRequest {

    @NotNull(message = "예약 시작 시간은 필수 값입니다.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime reservationStartAt;

    @NotNull(message = "예약 종료 시간은 필수 값입니다.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime reservationEndAt;

    private String destination;

    @NotBlank(message = "용도는 필수 값입니다.")
    private String purpose;

    private Integer attendees;
}
