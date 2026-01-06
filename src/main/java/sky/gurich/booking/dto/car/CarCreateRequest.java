package sky.gurich.booking.dto.car;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import sky.gurich.booking.entity.Car;
import sky.gurich.booking.entity.CarStatus;
import sky.gurich.booking.entity.CarType;

@Getter
@Setter
public class CarCreateRequest {

    @NotBlank(message = "차량번호 정보는 필수 값입니다.")
    @Pattern(regexp = "^(\\d{2,3}[가-힣]\\d{4})$", message = "차량 번호 형식이 올바르지 않습니다.")
    private String licensePlate;

    @NotNull(message = "수용 인원 정보는 필수 값입니다.")
    @Positive(message = "수용 인원은 1 이상이어야 합니다.")
    private int capacity;

    @NotNull(message = "차량 종류 정보는 필수 값입니다.")
    private CarType carType;

    @Size(max = 255, message = "설명은 최대 255자까지 입력할 수 있습니다.")
    private String description;

    private CarStatus status = CarStatus.AVAILABLE; // 기본값

    public Car toEntity() {
        return Car.builder()
                .licensePlate(this.licensePlate)
                .capacity(this.capacity)
                .carType(this.carType)
                .description(this.description)
                .status(this.status)
                .build();
    }
}
