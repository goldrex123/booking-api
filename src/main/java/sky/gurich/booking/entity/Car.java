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

    private String carNumber;

    private Integer capacity;

    @Enumerated(EnumType.STRING)
    private CarType carType;

    private String description;

    @Builder
    public Car(String carNumber, Integer capacity, CarType carType, String description) {
        this.carNumber = carNumber;
        this.capacity = capacity;
        this.carType = carType;
        this.description = description;
    }

    public void update(String carNumber, Integer capacity, CarType carType, String description) {
        this.capacity = capacity;
        this.carNumber = carNumber;
        this.carType = carType;
        this.description = description;
    }
}
