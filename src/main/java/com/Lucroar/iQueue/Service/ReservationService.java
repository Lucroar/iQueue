package com.Lucroar.iQueue.Service;

import com.Lucroar.iQueue.DTO.CustomerDTO;
import com.Lucroar.iQueue.Entity.Customer;
import com.Lucroar.iQueue.Entity.Reservation;
import com.Lucroar.iQueue.Entity.Status;
import com.Lucroar.iQueue.Repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public Reservation createReservation(Customer customer, Reservation reservation) {
        Reservation reserved = checkReservation(customer);
        if (reserved != null) return null;
        CustomerDTO customerDTO = new CustomerDTO(customer);
        customerDTO.setCustomer_id(customer.getCustomer_id());
        customerDTO.setUsername(customer.getUsername());
        reservation.setCustomer(customerDTO);
        reservation.setStatus(Status.CREATED);
        return reservationRepository.save(reservation);
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
