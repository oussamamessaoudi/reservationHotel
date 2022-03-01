package me.oussamamessaoudi.reservationhotel.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationEntity implements Comparable<ReservationEntity> {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private ReservationState state;

    @Override
    public int compareTo(ReservationEntity o) {
        return this.startDate.compareTo(o.startDate);
    }
}

