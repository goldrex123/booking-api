package sky.gurich.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sky.gurich.booking.common.ApiResponse;
import sky.gurich.booking.dto.CarCreateRequest;
import sky.gurich.booking.dto.CarResponse;
import sky.gurich.booking.dto.CarUpdateRequest;
import sky.gurich.booking.service.CarService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/car")
public class CarController {

    private final CarService carService;

    @GetMapping
    public ResponseEntity<?> getCar() {
        return ResponseEntity.ok(ApiResponse.success(carService.findCar()));
    }

    @PostMapping
    public ResponseEntity<?> insertCar(@RequestBody @Validated CarCreateRequest carCreateRequest) {
        CarResponse carResponse = carService.insertCar(carCreateRequest);
        return ResponseEntity.ok(ApiResponse.success(carResponse));
    }

    @PutMapping
    public ResponseEntity<?> updateCar(@RequestBody @Validated CarUpdateRequest carUpdateRequest) {
        CarResponse carResponse = carService.updateCar(carUpdateRequest);
        return ResponseEntity.ok(ApiResponse.success(carResponse));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCar(@PathVariable Long id) {
        carService.deleteCar(id);
        return ResponseEntity.ok(ApiResponse.success("차량이 성공적으로 삭제되었습니다."));
    }
}
