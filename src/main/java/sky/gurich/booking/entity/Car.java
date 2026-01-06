package sky.gurich.booking.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Car extends BaseTimeEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String licensePlate;

    private Integer capacity;

    @Enumerated(EnumType.STRING)
    private CarType carType;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CarStatus status = CarStatus.AVAILABLE;

    @Builder
    public Car(String licensePlate, Integer capacity, CarType carType, String description, CarStatus status) {
        this.licensePlate = licensePlate;
        this.capacity = capacity;
        this.carType = carType;
        this.description = description;
        this.status = status != null ? status : CarStatus.AVAILABLE;
    }

    public void update(String licensePlate, Integer capacity, CarType carType, String description, CarStatus status) {
        this.licensePlate = licensePlate;
        this.capacity = capacity;
        this.carType = carType;
        this.description = description;
        this.status = status;
    }
}
