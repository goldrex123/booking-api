package sky.gurich.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sky.gurich.booking.entity.Reservation;

import java.time.LocalDateTime;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("""
            select count(r) > 0
            from Reservation r
            where r.car.id = :carId
                and r.reservationStartAt < :endAt
                and r.reservationEndAt > :startAt 
    """)
    boolean existCarReservationConflict(@Param("carId") Long carId, @Param("startAt")LocalDateTime startAt, @Param("endAt") LocalDateTime endAt);

    @Query("""
            select count(r) > 0
            from Reservation r
            where r.room.id = :roomId
                and r.reservationStartAt < :endAt
                and r.reservationEndAt > :startAt 
    """)
    boolean existRoomReservationConflict(@Param("roomId") Long roomId, @Param("startAt")LocalDateTime startAt, @Param("endAt") LocalDateTime endAt);


}
