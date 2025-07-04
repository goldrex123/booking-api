package sky.gurich.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sky.gurich.booking.dto.CarCreateRequest;
import sky.gurich.booking.dto.CarResponse;
import sky.gurich.booking.entity.Car;
import sky.gurich.booking.repository.CarRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CarService {

    private final CarRepository carRepository;


    public CarResponse insertCar(CarCreateRequest carCreateRequest) {
        Car car = carCreateRequest.toEntity();
        carRepository.save(car);
        return CarResponse.toDto(car);
    }

}
