package me.oussamamessaoudi.reservationhotel.service;

import lombok.AllArgsConstructor;
import me.oussamamessaoudi.reservationhotel.entity.ReservationEntity;
import me.oussamamessaoudi.reservationhotel.entity.ReservationState;
import me.oussamamessaoudi.reservationhotel.model.Slot;
import me.oussamamessaoudi.reservationhotel.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class ReservationService {
    private static final LocalDate MIN_DATE = LocalDate.now().plusDays(1);
    private static final LocalDate MAX_DATE = MIN_DATE.plusDays(30);

    private ReservationRepository reservationRepository;

    public List<Slot> availableSlots(LocalDate startDate, LocalDate endDate) {
        startDate = Objects.isNull(startDate) || startDate.compareTo(MIN_DATE) < 0 ? MIN_DATE : startDate;
        endDate = Objects.isNull(endDate) || endDate.compareTo(MAX_DATE) > 0 ? MAX_DATE : endDate;

        AtomicReference<LocalDate> slotStartDate = new AtomicReference<>(startDate);
        return Stream.concat(
                        reservationRepository.getTakenSlots(startDate, endDate).stream(),
                        Stream.of(ReservationEntity.builder().startDate(endDate).build()))
                .map(reservation -> Slot.builder()
                        .startDate(slotStartDate.getAndSet(reservation.getEndDate()))
                        .endDate(reservation.getStartDate())
                        .build())
                .filter(slot -> !slot.getStartDate().equals(slot.getEndDate()))
                .collect(Collectors.toList());
    }

    public void makeReservation(LocalDate startDate, LocalDate endDate) {
        boolean available = reservationRepository.isAvailable(startDate, endDate);
        if(available) {
            reservationRepository.save(ReservationEntity.builder().startDate(startDate).endDate(endDate).state(ReservationState.VALID).build());
        }
    }

    public void cancelReservation(long id) {
        reservationRepository.cancelReservation(id);
    }

    public void modifyReservation(long id, LocalDate startDate, LocalDate endDate) {
        makeReservation(startDate, endDate);
        cancelReservation(id);
    }
}

