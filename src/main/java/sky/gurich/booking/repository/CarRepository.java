package sky.gurich.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sky.gurich.booking.entity.Car;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
}
