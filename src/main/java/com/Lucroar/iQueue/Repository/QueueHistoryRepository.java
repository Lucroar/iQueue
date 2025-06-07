package com.Lucroar.iQueue.Repository;

import com.Lucroar.iQueue.Entity.QueueHistory;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface QueueHistoryRepository extends MongoRepository<QueueHistory, String> {
}
