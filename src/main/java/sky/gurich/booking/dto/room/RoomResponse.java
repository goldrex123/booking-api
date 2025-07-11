package sky.gurich.booking.dto.room;

import lombok.Builder;
import lombok.Getter;
import sky.gurich.booking.entity.Room;
import sky.gurich.booking.entity.RoomLocation;

@Getter
public class RoomResponse {

    private Long id;
    private String roomName;
    private RoomLocation roomLocation;
    private String description;

    @Builder
    public RoomResponse(Long id, String roomName, RoomLocation roomLocation, String description) {
        this.id = id;
        this.roomName = roomName;
        this.roomLocation = roomLocation;
        this.description = description;
    }

    public static RoomResponse toDto(Room room) {
        return RoomResponse.builder()
                .id(room.getId())
                .roomName(room.getRoomName())
                .roomLocation(room.getRoomLocation())
                .description(room.getDescription())
                .build();
    }
}
