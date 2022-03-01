package me.oussamamessaoudi.reservationhotel.model;

import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Slot {
    private LocalDate startDate;
    private LocalDate endDate;
}
