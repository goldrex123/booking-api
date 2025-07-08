package sky.gurich.booking.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import sky.gurich.booking.entity.Room;
import sky.gurich.booking.entity.RoomLocation;

@Getter
@Setter
public class RoomCreateRequest {

    @NotNull(message = "부속실 이름은 필수 값입니다.")
    private String roomName;
    @NotNull(message = "부속실 위치는 필수 값입니다.")
    private RoomLocation roomLocation;
    @Size(max = 255, message = "설명은 최대 255자까지 입력할 수 있습니다.")
    private String description;

    public Room toEntity() {
        return Room.builder()
                .roomName(this.roomName)
                .roomLocation(this.roomLocation)
                .description(this.description)
                .build();
    }

}
