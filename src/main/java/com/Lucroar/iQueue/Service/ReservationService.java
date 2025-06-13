package com.Lucroar.iQueue.Service;

import com.Lucroar.iQueue.DTO.CustomerDTO;
import com.Lucroar.iQueue.Entity.Customer;
import com.Lucroar.iQueue.Entity.Reservation;
import com.Lucroar.iQueue.Entity.Status;
import com.Lucroar.iQueue.Exceptions.ReservationResult;
import com.Lucroar.iQueue.Repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public ReservationResult createReservation(Customer customer, Reservation reservation) {
        Reservation existing = checkReservation(customer);
        if (existing != null) {
            return new ReservationResult(false, "Already created a reservation", null);
        }

        // Check 3-day advance requirement
        if (reservation.getReservation_date().isBefore(LocalDateTime.now().plusDays(3))) {
            return new ReservationResult(false, "Reservation must be made at least 3 days in advance", null);
        }

        CustomerDTO customerDTO = new CustomerDTO(customer);
        customerDTO.setCustomerId(customer.getId());
        customerDTO.setUsername(customer.getUsername());
        reservation.setCustomer(customerDTO);
        reservation.setStatus(Status.CREATED);

        Reservation saved = reservationRepository.save(reservation);
        return new ReservationResult(true, "Reservation created successfully", saved);
    }

    public Reservation cancelReservation(Reservation reservation) {
        Optional<Reservation> reservationOptional = reservationRepository.findById(reservation.getReservation_id());
        if (reservationOptional.isPresent()) {
            Reservation reservation1 = reservationOptional.get();
            reservation1.setStatus(Status.CANCELLED);
            return reservationRepository.save(reservation1);
        }
        return null;
    }

    public Reservation checkReservation(Customer customer) {
        return reservationRepository.findByCustomerUsernameAndStatus(customer.getUsername(), Status.CREATED);
    }
}
