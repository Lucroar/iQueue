package com.Lucroar.iQueue.Repository;

import com.Lucroar.iQueue.Entity.Cashier;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CashierRepository extends MongoRepository<Cashier,String> {
    Optional<Cashier> findByUsername(String username);
}
