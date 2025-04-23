package com.Lucroar.iQueue.Controller;

import com.Lucroar.iQueue.Entity.Customer;
import com.Lucroar.iQueue.Entity.Reservation;
import com.Lucroar.iQueue.Service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/reservation")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createReservation(@AuthenticationPrincipal Customer customer, @RequestBody Reservation reservation) {
        Reservation reservationEntity = reservationService.createReservation(customer, reservation);
        if (reservationEntity == null) {
            return ResponseEntity.status(409).body(Collections.singletonMap("msg", "Already Created a Reservation"));
        }
        return ResponseEntity.ok(reservationEntity);
    }

    @PatchMapping("/cancel")
    public ResponseEntity<?> cancelReservation(@RequestBody Reservation reservation) {
        return ResponseEntity.ok(reservationService.cancelReservation(reservation));
    }

    @GetMapping("/check")
    public ResponseEntity<?> checkReservation(@AuthenticationPrincipal Customer customer) {
        return ResponseEntity.ok(reservationService.checkReservation(customer));
    }
}
