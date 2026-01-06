package sky.gurich.booking.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sky.gurich.booking.dto.car.CarResponse;
import sky.gurich.booking.dto.reservation.*;
import sky.gurich.booking.dto.room.RoomResponse;
import sky.gurich.booking.entity.*;
import sky.gurich.booking.repository.CarRepository;
import sky.gurich.booking.repository.MemberRepository;
import sky.gurich.booking.repository.ReservationRepository;
import sky.gurich.booking.repository.RoomRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
        // 1. 시간 유효성 검증
        if (request.getReservationEndAt().isBefore(request.getReservationStartAt())) {
            throw new IllegalArgumentException("예약 종료 시간은 시작 시간보다 이후여야 합니다.");
        }

        // 2. 사용자 조회
        Member member = getMemberOrThrow(request.getUserId());
        Car car = null;
        Room room = null;

        if (request.getReservationType() == ReservationType.CAR) {
            // 차량 예약
            car = carRepository.findById(request.getId())
                    .orElseThrow(() -> new EntityNotFoundException("차량 정보가 없습니다."));

            // 차량 상태 확인
            if (car.getStatus() != CarStatus.AVAILABLE) {
                throw new IllegalStateException("현재 사용 가능한 차량이 아닙니다.");
            }

            // 시간 중복 체크
            if (reservationRepository.existCarReservationConflict(car.getId(), request.getReservationStartAt(), request.getReservationEndAt())) {
                throw new IllegalStateException("해당 시간에 이미 차량이 예약되어 있습니다.");
            }

            // destination 필수 체크
            if (request.getDestination() == null || request.getDestination().isBlank()) {
                throw new IllegalArgumentException("차량 예약 시 목적지는 필수입니다.");
            }

        } else {
            // 부속실 예약
            room = roomRepository.findById(request.getId())
                    .orElseThrow(() -> new EntityNotFoundException("부속실 정보가 없습니다."));

            // 부속실 상태 확인
            if (room.getStatus() != RoomStatus.AVAILABLE) {
                throw new IllegalStateException("현재 사용 가능한 부속실이 아닙니다.");
            }

            // 시간 중복 체크
            if (reservationRepository.existRoomReservationConflict(room.getId(), request.getReservationStartAt(), request.getReservationEndAt())) {
                throw new IllegalStateException("해당 시간에 이미 부속실이 예약되어 있습니다.");
            }

            // attendees 필수 체크
            if (request.getAttendees() == null || request.getAttendees() <= 0) {
                throw new IllegalArgumentException("부속실 예약 시 참석 인원은 필수이며 1명 이상이어야 합니다.");
            }
        }

        // 3. 예약 생성
        Reservation reservation = request.toEntity(member, car, room);
        reservationRepository.save(reservation);
        return ReservationResponse.toDto(reservation);
    }

    private Member getMemberOrThrow(Long userId) {
        return memberRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("해당 사용자 정보가 없습니다."));
    }

    public List<ReservationResponse> findReserve(ReservationSearchRequest request) {
        List<Reservation> result;

        if(request.getReservationType() == ReservationType.CAR) {
            result = reservationRepository.findCarReservation(request.getId(), request.getSearchStartAt(), request.getSearchEndAt());
        } else {
            result = reservationRepository.findRoomReservation(request.getId(), request.getSearchStartAt(), request.getSearchEndAt());
        }

        return result.stream()
                .map(ReservationResponse::toDto)
                .toList();
    }

    @Transactional
    public void cancelReserve(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("예약 정보가 없습니다."));

        reservationRepository.delete(reservation);
    }

    // 예약 수정
    @Transactional
    public ReservationResponse updateReservation(Long id, ReservationUpdateRequest request) {
        // 1. 예약 조회
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("예약 정보가 없습니다."));

        // 2. 시간 변경 시 중복 체크
        if (!reservation.getReservationStartAt().equals(request.getReservationStartAt()) ||
            !reservation.getReservationEndAt().equals(request.getReservationEndAt())) {

            // 시간 유효성 검증
            if (request.getReservationEndAt().isBefore(request.getReservationStartAt())) {
                throw new IllegalArgumentException("예약 종료 시간은 시작 시간보다 이후여야 합니다.");
            }

            if (reservation.getReservationType() == ReservationType.CAR) {
                if (reservationRepository.existCarReservationConflictExcludingSelf(
                        reservation.getCar().getId(),
                        request.getReservationStartAt(),
                        request.getReservationEndAt(),
                        id)) {
                    throw new IllegalStateException("해당 시간에 이미 차량이 예약되어 있습니다.");
                }
            } else {
                if (reservationRepository.existRoomReservationConflictExcludingSelf(
                        reservation.getRoom().getId(),
                        request.getReservationStartAt(),
                        request.getReservationEndAt(),
                        id)) {
                    throw new IllegalStateException("해당 시간에 이미 부속실이 예약되어 있습니다.");
                }
            }
        }

        // 3. 타입별 필수 필드 검증
        if (reservation.getReservationType() == ReservationType.CAR) {
            if (request.getDestination() == null || request.getDestination().isBlank()) {
                throw new IllegalArgumentException("차량 예약 시 목적지는 필수입니다.");
            }
        } else {
            if (request.getAttendees() == null || request.getAttendees() <= 0) {
                throw new IllegalArgumentException("부속실 예약 시 참석 인원은 필수이며 1명 이상이어야 합니다.");
            }
        }

        // 4. 업데이트
        reservation.update(
                request.getReservationStartAt(),
                request.getReservationEndAt(),
                request.getDestination(),
                request.getPurpose(),
                request.getAttendees()
        );

        return ReservationResponse.toDto(reservation);
    }

    // 사용자별 예약 조회
    public List<ReservationResponse> findByUserId(Long userId) {
        Member member = getMemberOrThrow(userId);
        List<Reservation> reservations = reservationRepository.findByMemberId(userId);

        return reservations.stream()
                .map(ReservationResponse::toDto)
                .toList();
    }

    // 날짜 범위 예약 조회
    public List<ReservationResponse> findByDateRange(LocalDateTime start, LocalDateTime end, ReservationType type) {
        List<Reservation> reservations;

        if (type == null) {
            // 전체 조회
            reservations = reservationRepository.findByDateRange(start, end);
        } else if (type == ReservationType.CAR) {
            reservations = reservationRepository.findCarReservationsByDateRange(start, end);
        } else {
            reservations = reservationRepository.findRoomReservationsByDateRange(start, end);
        }

        return reservations.stream()
                .map(ReservationResponse::toDto)
                .toList();
    }

    // 다중 예약
    @Transactional
    public MultiReservationResponse multiReserve(MultiReservationRequest request) {
        // 1. 기본 유효성 검증
        if (request.getReservationEndAt().isBefore(request.getReservationStartAt())) {
            throw new IllegalArgumentException("예약 종료 시간은 시작 시간보다 이후여야 합니다.");
        }

        List<MultiReservationResponse.ReservationResult> successResults = new ArrayList<>();
        List<MultiReservationResponse.ReservationResult> failedResults = new ArrayList<>();

        Member member = getMemberOrThrow(request.getUserId());

        // 2. 각 대상에 대해 예약 시도
        for (Long targetId : request.getIds()) {
            try {
                // 개별 예약 생성
                ReservationCreateRequest singleRequest = ReservationCreateRequest.builder()
                        .id(targetId)
                        .userId(request.getUserId())
                        .reservationType(request.getReservationType())
                        .reservationStartAt(request.getReservationStartAt())
                        .reservationEndAt(request.getReservationEndAt())
                        .destination(request.getDestination())
                        .purpose(request.getPurpose())
                        .attendees(request.getAttendees())
                        .build();

                ReservationResponse reservation = reserve(singleRequest);

                // 성공 결과 추가
                String targetName = getTargetName(targetId, request.getReservationType());
                successResults.add(MultiReservationResponse.ReservationResult.builder()
                        .targetId(targetId)
                        .targetName(targetName)
                        .reservation(reservation)
                        .success(true)
                        .build());

            } catch (Exception e) {
                // 실패 결과 추가
                String targetName = getTargetName(targetId, request.getReservationType());
                failedResults.add(MultiReservationResponse.ReservationResult.builder()
                        .targetId(targetId)
                        .targetName(targetName)
                        .errorMessage(e.getMessage())
                        .success(false)
                        .build());
            }
        }

        // 3. 결과 반환
        return MultiReservationResponse.builder()
                .successResults(successResults)
                .failedResults(failedResults)
                .totalCount(request.getIds().size())
                .successCount(successResults.size())
                .failedCount(failedResults.size())
                .build();
    }

    // Helper 메서드
    private String getTargetName(Long targetId, ReservationType type) {
        if (type == ReservationType.CAR) {
            return carRepository.findById(targetId)
                    .map(Car::getLicensePlate)
                    .orElse("알 수 없는 차량");
        } else {
            return roomRepository.findById(targetId)
                    .map(Room::getRoomName)
                    .orElse("알 수 없는 부속실");
        }
    }
}
