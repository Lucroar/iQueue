package com.Lucroar.iQueue.Repository;

import com.Lucroar.iQueue.Entity.QueueEntry;
import com.Lucroar.iQueue.Entity.Status;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QueueRepository extends MongoRepository<QueueEntry, String> {
    Optional<QueueEntry> findByCustomerUsernameAndStatusIn(String username, List<Status> status);
}
