package com.Lucroar.iQueue.Exceptions;

import com.Lucroar.iQueue.Entity.Reservation;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReservationResult {
    private boolean success;
    private String message;
    private Reservation reservation;
}
