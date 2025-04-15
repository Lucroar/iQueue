package com.Lucroar.iQueue.Repository;

import com.Lucroar.iQueue.Entity.Queue;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QueueRepository extends MongoRepository<Queue, String> {

}
