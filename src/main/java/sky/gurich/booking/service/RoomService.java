package sky.gurich.booking.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sky.gurich.booking.dto.room.RoomCreateRequest;
import sky.gurich.booking.dto.room.RoomResponse;
import sky.gurich.booking.dto.room.RoomUpdateRequest;
import sky.gurich.booking.entity.Room;
import sky.gurich.booking.repository.RoomRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

    public List<RoomResponse> findRoom() {
        List<Room> rooms = roomRepository.findAll(Sort.by(Sort.Direction.ASC, "roomLocation"));
        return rooms.stream()
                .map(RoomResponse::toDto)
                .toList();
    }
    public RoomResponse findRoomById(Long id) {
        Room findRoom = getRoomOrThrow(id);
        return RoomResponse.toDto(findRoom);
    }

    @Transactional
    public RoomResponse insertRoom(RoomCreateRequest roomCreateRequest) {
        Room room = roomCreateRequest.toEntity();
        roomRepository.save(room);

        return RoomResponse.toDto(room);
    }


    @Transactional
    public RoomResponse updateRoom(RoomUpdateRequest roomUpdateRequest) {
        Room room = getRoomOrThrow(roomUpdateRequest.getId());
        room.update(
                roomUpdateRequest.getRoomName(),
                roomUpdateRequest.getRoomLocation(),
                roomUpdateRequest.getDescription(),
                roomUpdateRequest.getStatus()
        );
        return RoomResponse.toDto(room);
    }

    @Transactional
    public void deleteRoom(Long id) {
        Room room = getRoomOrThrow(id);
        roomRepository.delete(room);
    }

    private Room getRoomOrThrow(Long id) {
        return roomRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("부속실 정보가 없습니다"));
    }

}
