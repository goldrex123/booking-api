package sky.gurich.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sky.gurich.booking.common.ApiResponse;
import sky.gurich.booking.dto.CarCreateRequest;
import sky.gurich.booking.dto.CarResponse;
import sky.gurich.booking.service.CarService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/car")
public class CarController {

    private final CarService carService;

    @GetMapping
    public ResponseEntity<?> getCar() {

        return null;
    }

    @PostMapping
    public ResponseEntity<?> postCar(@RequestBody @Validated CarCreateRequest carCreateRequest) {
        CarResponse carResponse = carService.insertCar(carCreateRequest);
        ApiResponse<CarResponse> success = ApiResponse.success(carResponse);
        return ResponseEntity.ok(success);
    }
}
