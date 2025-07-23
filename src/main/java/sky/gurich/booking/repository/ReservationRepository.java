package sky.gurich.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.keyvalue.repository.config.QueryCreatorType;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sky.gurich.booking.entity.Reservation;

import java.time.LocalDateTime;
import java.util.List;

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

    @Query("""
            select r
            from Reservation r
                join fetch r.member m
                join fetch r.car c
            where r.car.id = :carId
                and r.reservationStartAt <= :startAt
                and r.reservationEndAt >= :endAt
            order by r.reservationStartAt asc 
    """
    )
    List<Reservation> findCarReservation(@Param("carId") Long carId, @Param("startAt") LocalDateTime searchStartAt, @Param("endAt") LocalDateTime searchEndAt);

    @Query("""
            select r
            from Reservation r
                join fetch r.member m
                join fetch r.room rm
            where r.car.id = :roomId
                and r.reservationStartAt <= :startAt
                and r.reservationEndAt >= :endAt
            order by r.reservationStartAt asc 
    """
    )
    List<Reservation> findRoomReservation(@Param("roomId") Long roomId, @Param("startAt") LocalDateTime searchStartAt, @Param("endAt") LocalDateTime searchEndAt);
}
