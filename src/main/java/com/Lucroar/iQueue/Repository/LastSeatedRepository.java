package com.Lucroar.iQueue.Repository;

import com.Lucroar.iQueue.Entity.LastSeated;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface LastSeatedRepository extends MongoRepository<LastSeated, String> {
    Optional<LastSeated> findLastSeatedByTier(int tier);
}
