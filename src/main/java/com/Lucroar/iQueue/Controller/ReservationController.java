package com.Lucroar.iQueue.Controller;

import com.Lucroar.iQueue.Entity.Customer;
import com.Lucroar.iQueue.Entity.Reservation;
import com.Lucroar.iQueue.Service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reservation")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createReservation(@AuthenticationPrincipal Customer customer, @RequestBody Reservation reservation) {
        return ResponseEntity.ok(reservationService.createReservation(customer, reservation));
    }
}
