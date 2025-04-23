package com.Lucroar.iQueue.Service;

import com.Lucroar.iQueue.DTO.CustomerDTO;
import com.Lucroar.iQueue.Entity.Customer;
import com.Lucroar.iQueue.Entity.Reservation;
import com.Lucroar.iQueue.Entity.Status;
import com.Lucroar.iQueue.Repository.ReservationRepository;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public Reservation createReservation(Customer customer, Reservation reservation) {
        CustomerDTO customerDTO = new CustomerDTO(customer);
        customerDTO.setCustomer_id(customer.getCustomer_id());
        customerDTO.setUsername(customer.getUsername());
        reservation.setCustomer(customerDTO);
        reservation.setStatus(Status.CREATED);
        return reservationRepository.save(reservation);
    }
}
