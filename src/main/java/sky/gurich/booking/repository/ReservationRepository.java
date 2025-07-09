package sky.gurich.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sky.gurich.booking.entity.Reservation;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {


}
