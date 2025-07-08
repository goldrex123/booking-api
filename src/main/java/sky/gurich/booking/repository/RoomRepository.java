package sky.gurich.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sky.gurich.booking.entity.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
}
