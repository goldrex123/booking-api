package sky.gurich.booking.dto.reservation;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MultiReservationResponse {

    private List<ReservationResult> successResults; // 성공한 예약 목록
    private List<ReservationResult> failedResults;  // 실패한 예약 목록
    private int totalCount;
    private int successCount;
    private int failedCount;

    @Getter
    @Builder
    public static class ReservationResult {
        private Long targetId; // carId 또는 roomId
        private String targetName; // car/room 이름
        private ReservationResponse reservation; // 성공 시
        private String errorMessage; // 실패 시
        private boolean success;
    }
}
