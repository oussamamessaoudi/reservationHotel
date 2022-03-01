package me.oussamamessaoudi.reservationhotel.repository;

import jdk.jfr.Registered;
import me.oussamamessaoudi.reservationhotel.entity.ReservationEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.TreeSet;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {

    @Query("FROM ReservationEntity res WHERE res.endDate >= :minDate OR res.startDate < :maxDate order by res.startDate")
    List<ReservationEntity> getTakenSlots(LocalDate minDate, LocalDate maxDate);

    default boolean isAvailable(LocalDate startDate, LocalDate endDate) {
        List<ReservationEntity> takenHelper = overlappedReservation(startDate, endDate, PageRequest.of(0, 1));
        return takenHelper.isEmpty();
    }

    @Query("FROM ReservationEntity res WHERE (:startDate >= res.startDate AND  :startDate < res.endDate) OR (:endDate >= res.startDate AND :endDate < res.endDate)")
    List<ReservationEntity> overlappedReservation(LocalDate startDate, LocalDate endDate, Pageable pageable);

    @Modifying
    @Query("UPDATE ReservationEntity res SET res.state = me.oussamamessaoudi.reservationhotel.entity.ReservationState.DELETED WHERE res.id = :id ")
    void cancelReservation(long id);

}