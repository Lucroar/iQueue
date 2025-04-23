package com.Lucroar.iQueue.Repository;

import com.Lucroar.iQueue.Entity.Reservation;
import com.Lucroar.iQueue.Entity.Status;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ReservationRepository extends MongoRepository<Reservation, String> {
    Reservation findByCustomerUsernameAndStatus(String customerId, Status status);
}
