package com.Lucroar.iQueue.Repository;

import com.Lucroar.iQueue.Entity.Reservation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends MongoRepository<Reservation, Integer> {

}
