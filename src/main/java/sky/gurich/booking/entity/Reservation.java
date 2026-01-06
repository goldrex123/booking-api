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
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id")
    private Car car;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    private LocalDateTime reservationStartAt;
    private LocalDateTime reservationEndAt;

    private String destination;  // 목적지 (차량 예약용)

    @Column(nullable = false)
    private String purpose;       // 용도 (공통)

    private Integer attendees;    // 참석인원 (부속실 예약용)

    @Builder
    public Reservation(ReservationType reservationType, Member member, Car car, Room room,
                      LocalDateTime reservationStartAt, LocalDateTime reservationEndAt,
                      String destination, String purpose, Integer attendees) {
        this.reservationType = reservationType;
        this.member = member;
        this.car = car;
        this.room = room;
        this.reservationStartAt = reservationStartAt;
        this.reservationEndAt = reservationEndAt;
        this.destination = destination;
        this.purpose = purpose;
        this.attendees = attendees;
    }

    public void update(LocalDateTime reservationStartAt, LocalDateTime reservationEndAt,
                      String destination, String purpose, Integer attendees) {
        this.reservationStartAt = reservationStartAt;
        this.reservationEndAt = reservationEndAt;
        this.destination = destination;
        this.purpose = purpose;
        this.attendees = attendees;
    }
}
