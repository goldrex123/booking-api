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
                and r.reservationStartAt <= :endAt
                and r.reservationEndAt >= :startAt
            order by r.reservationStartAt asc
    """
    )
    List<Reservation> findCarReservation(@Param("carId") Long carId, @Param("startAt") LocalDateTime searchStartAt, @Param("endAt") LocalDateTime searchEndAt);

    @Query("""
            select r
            from Reservation r
                join fetch r.member m
                join fetch r.room rm
            where r.room.id = :roomId
                and r.reservationStartAt <= :endAt
                and r.reservationEndAt >= :startAt
            order by r.reservationStartAt asc
    """
    )
    List<Reservation> findRoomReservation(@Param("roomId") Long roomId, @Param("startAt") LocalDateTime searchStartAt, @Param("endAt") LocalDateTime searchEndAt);

    // 자기 자신을 제외한 차량 예약 중복 체크 (수정 시 사용)
    @Query("""
            select count(r) > 0
            from Reservation r
            where r.car.id = :carId
                and r.reservationStartAt < :endAt
                and r.reservationEndAt > :startAt
                and r.id != :excludeId
    """)
    boolean existCarReservationConflictExcludingSelf(@Param("carId") Long carId,
                                                      @Param("startAt") LocalDateTime startAt,
                                                      @Param("endAt") LocalDateTime endAt,
                                                      @Param("excludeId") Long excludeId);

    // 자기 자신을 제외한 부속실 예약 중복 체크 (수정 시 사용)
    @Query("""
            select count(r) > 0
            from Reservation r
            where r.room.id = :roomId
                and r.reservationStartAt < :endAt
                and r.reservationEndAt > :startAt
                and r.id != :excludeId
    """)
    boolean existRoomReservationConflictExcludingSelf(@Param("roomId") Long roomId,
                                                       @Param("startAt") LocalDateTime startAt,
                                                       @Param("endAt") LocalDateTime endAt,
                                                       @Param("excludeId") Long excludeId);

    // 사용자별 예약 조회
    @Query("""
            select r
            from Reservation r
                left join fetch r.member m
                left join fetch r.car c
                left join fetch r.room rm
            where r.member.id = :memberId
            order by r.reservationStartAt desc
    """)
    List<Reservation> findByMemberId(@Param("memberId") Long memberId);

    // 날짜 범위 예약 조회 (전체)
    @Query("""
            select r
            from Reservation r
                left join fetch r.member m
                left join fetch r.car c
                left join fetch r.room rm
            where r.reservationStartAt <= :endAt
                and r.reservationEndAt >= :startAt
            order by r.reservationStartAt asc
    """)
    List<Reservation> findByDateRange(@Param("startAt") LocalDateTime startAt,
                                       @Param("endAt") LocalDateTime endAt);

    // 날짜 범위 차량 예약 조회
    @Query("""
            select r
            from Reservation r
                join fetch r.member m
                join fetch r.car c
            where r.reservationType = 'CAR'
                and r.reservationStartAt <= :endAt
                and r.reservationEndAt >= :startAt
            order by r.reservationStartAt asc
    """)
    List<Reservation> findCarReservationsByDateRange(@Param("startAt") LocalDateTime startAt,
                                                      @Param("endAt") LocalDateTime endAt);

    // 날짜 범위 부속실 예약 조회
    @Query("""
            select r
            from Reservation r
                join fetch r.member m
                join fetch r.room rm
            where r.reservationType = 'ROOM'
                and r.reservationStartAt <= :endAt
                and r.reservationEndAt >= :startAt
            order by r.reservationStartAt asc
    """)
    List<Reservation> findRoomReservationsByDateRange(@Param("startAt") LocalDateTime startAt,
                                                       @Param("endAt") LocalDateTime endAt);
}
