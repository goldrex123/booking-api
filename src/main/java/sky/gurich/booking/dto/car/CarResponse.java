package sky.gurich.booking.dto.car;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import sky.gurich.booking.entity.Car;
import sky.gurich.booking.entity.CarStatus;
import sky.gurich.booking.entity.CarType;

@Getter
@Setter
public class CarResponse {
    private Long id;
    private String licensePlate;
    private Integer capacity;
    private CarType carType;
    private String description;
    private CarStatus status;

    @Builder
    public CarResponse(Long id, String licensePlate, Integer capacity, CarType carType, String description, CarStatus status) {
        this.id = id;
        this.licensePlate = licensePlate;
        this.capacity = capacity;
        this.carType = carType;
        this.description = description;
        this.status = status;
    }

    public static CarResponse toDto(Car car) {
        return CarResponse.builder()
                .id(car.getId())
                .licensePlate(car.getLicensePlate())
                .capacity(car.getCapacity())
                .carType(car.getCarType())
                .description(car.getDescription())
                .status(car.getStatus())
                .build();
    }
}
