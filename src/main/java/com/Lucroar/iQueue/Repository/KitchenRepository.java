package com.Lucroar.iQueue.Repository;

import com.Lucroar.iQueue.Entity.KitchenStaff;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface KitchenRepository extends MongoRepository<KitchenStaff, String> {
    Optional<KitchenStaff> findByUsername(String id);
}
