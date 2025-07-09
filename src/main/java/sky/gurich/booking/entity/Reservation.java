package sky.gurich.booking.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ReservationType reservationType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id")
    private Car car;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    private LocalDateTime reservationStartAt;
    private LocalDateTime reservationEndAt;

    @Builder
    public Reservation(ReservationType reservationType, User user, Car car, Room room, LocalDateTime reservationStartAt, LocalDateTime reservationEndAt) {
        this.reservationType = reservationType;
        this.user = user;
        this.car = car;
        this.room = room;
        this.reservationStartAt = reservationStartAt;
        this.reservationEndAt = reservationEndAt;
    }
}
