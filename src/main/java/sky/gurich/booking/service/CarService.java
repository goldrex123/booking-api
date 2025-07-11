package sky.gurich.booking.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sky.gurich.booking.dto.car.CarCreateRequest;
import sky.gurich.booking.dto.car.CarResponse;
import sky.gurich.booking.dto.car.CarUpdateRequest;
import sky.gurich.booking.entity.Car;
import sky.gurich.booking.repository.CarRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CarService {

    private final CarRepository carRepository;

    public List<CarResponse> findCar() {
        List<Car> carNumber = carRepository.findAll(Sort.by(Sort.Direction.ASC, "carNumber"));

        return carNumber.stream()
                .map(CarResponse::toDto)
                .toList();
    }

    public CarResponse findCarById(Long id) {
        Car car = getCarOrThrow(id);

        return CarResponse.toDto(car);
    }

    @Transactional
    public CarResponse insertCar(CarCreateRequest carCreateRequest) {
        Car car = carCreateRequest.toEntity();
        carRepository.save(car);
        return CarResponse.toDto(car);
    }

    @Transactional
    public CarResponse updateCar(CarUpdateRequest carUpdateRequest) {
        Car car = getCarOrThrow(carUpdateRequest.getId());
        car.update(
                carUpdateRequest.getCarNumber(),
                carUpdateRequest.getCapacity(),
                carUpdateRequest.getCarType(),
                carUpdateRequest.getDescription()
        );

        return CarResponse.toDto(car);
    }

    @Transactional
    public void deleteCar(Long id) {
        Car car = getCarOrThrow(id);

        carRepository.delete(car);
    }


    private Car getCarOrThrow(Long id) {
        return carRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("차량 정보가 없습니다."));
    }


}
