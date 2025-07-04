package sky.gurich.booking.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import sky.gurich.booking.entity.Car;
import sky.gurich.booking.entity.CarType;

@Getter
@Setter
public class CarResponse {
    private Long id;
    private String carNumber;
    private Integer capacity;
    private CarType carType;
    private String description;

    @Builder
    public CarResponse(Long id, String carNumber, Integer capacity, CarType carType, String description) {
        this.id = id;
        this.carNumber = carNumber;
        this.capacity = capacity;
        this.carType = carType;
        this.description = description;
    }

    public static CarResponse toDto(Car car) {
        return CarResponse.builder()
                .id(car.getId())
                .carNumber(car.getCarNumber())
                .capacity(car.getCapacity())
                .carType(car.getCarType())
                .description(car.getDescription())
                .build();
    }
}
