package sky.gurich.booking.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sky.gurich.booking.dto.car.CarResponse;
import sky.gurich.booking.dto.reservation.ReservationCreateRequest;
import sky.gurich.booking.dto.reservation.ReservationResponse;
import sky.gurich.booking.dto.room.RoomResponse;
import sky.gurich.booking.entity.*;
import sky.gurich.booking.repository.CarRepository;
import sky.gurich.booking.repository.MemberRepository;
import sky.gurich.booking.repository.ReservationRepository;
import sky.gurich.booking.repository.RoomRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;
    private final CarRepository carRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ReservationResponse reserve(ReservationCreateRequest request) {
        Member member = getMemberOrThrow(request.getUserId());
        Car car = null;
        Room room = null;
        if (request.getReservationType() == ReservationType.ROOM) {
            room = roomRepository.findById(request.getId())
                    .orElseThrow(() -> new EntityNotFoundException("부속실 정보가 없습니다."));

            if(reservationRepository.existRoomReservationConflict(room.getId(), request.getReservationStartAt(), request.getReservationEndAt()))
                throw new IllegalStateException("해당 시간에 이미 부속실이 예약되어 있습니다.");
        } else {
            car = carRepository.findById(request.getId())
                    .orElseThrow(() -> new EntityNotFoundException("차량 정보가 없습니다."));
            if(reservationRepository.existCarReservationConflict(car.getId(), request.getReservationStartAt(), request.getReservationEndAt()))
                throw new IllegalStateException("해당 시간에 이미 차량이 예약되어 있습니다.");
        }

        if (request.getReservationEndAt().isBefore(request.getReservationStartAt())) {
            throw new IllegalArgumentException("예약 종료 시간은 시작 시간보다 이후여야 합니다.");
        }

        Reservation reservation = request.toEntity(member, car, room);
        reservationRepository.save(reservation);
        return ReservationResponse.toDto(reservation);
    }

    private Member getMemberOrThrow(Long userId) {
        return memberRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("해당 사용자 정보가 없습니다."));
    }
}
