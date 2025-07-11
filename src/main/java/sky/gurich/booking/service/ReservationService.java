package sky.gurich.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sky.gurich.booking.dto.reservation.ReservationCreateRequest;
import sky.gurich.booking.dto.reservation.ReservationResponse;
import sky.gurich.booking.repository.ReservationRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository repository;


    public ReservationResponse insertReservation(ReservationCreateRequest request) {



        return null;
    }
}
