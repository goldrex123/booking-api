package sky.gurich.booking.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    @Builder
    public Room(String roomName, RoomLocation roomLocation, String description) {
        this.roomName = roomName;
        this.roomLocation = roomLocation;
        this.description = description;
    }

    public void update(String roomName, RoomLocation roomLocation, String description) {
        this.roomName = roomName;
        this.roomLocation = roomLocation;
        this.description = description;
    }
}
