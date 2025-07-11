package sky.gurich.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sky.gurich.booking.common.ApiResponse;
import sky.gurich.booking.dto.room.RoomCreateRequest;
import sky.gurich.booking.dto.room.RoomResponse;
import sky.gurich.booking.dto.room.RoomUpdateRequest;
import sky.gurich.booking.service.RoomService;

@RestController
@RequestMapping("/api/room")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @GetMapping
    public ResponseEntity<?> getRoom() {
        return ResponseEntity.ok(ApiResponse.success(roomService.findRoom()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRoomById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(roomService.findRoomById(id)));
    }

    @PostMapping
    public ResponseEntity<?> insertRoom(@RequestBody @Validated RoomCreateRequest roomCreateRequest) {

        RoomResponse roomResponse = roomService.insertRoom(roomCreateRequest);
        return ResponseEntity.ok(ApiResponse.success(roomResponse));
    }

    @PutMapping
    public ResponseEntity<?> updateRoom(@RequestBody @Validated RoomUpdateRequest roomUpdateRequest) {
        RoomResponse response = roomService.updateRoom(roomUpdateRequest);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);

        return ResponseEntity.ok(ApiResponse.success("부속실이 성공적으로 삭제되었습니다"));
    }
}