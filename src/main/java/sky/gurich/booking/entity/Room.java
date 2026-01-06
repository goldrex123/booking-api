package sky.gurich.booking.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access =  AccessLevel.PROTECTED)
public class Room extends BaseTimeEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roomName;

    private RoomLocation roomLocation;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomStatus status = RoomStatus.AVAILABLE;

    @Builder
    public Room(String roomName, RoomLocation roomLocation, String description, RoomStatus status) {
        this.roomName = roomName;
        this.roomLocation = roomLocation;
        this.description = description;
        this.status = status != null ? status : RoomStatus.AVAILABLE;
    }

    public void update(String roomName, RoomLocation roomLocation, String description, RoomStatus status) {
        this.roomName = roomName;
        this.roomLocation = roomLocation;
        this.description = description;
        this.status = status;
    }
}
