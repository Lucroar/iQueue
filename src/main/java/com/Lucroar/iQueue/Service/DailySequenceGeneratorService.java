package com.Lucroar.iQueue.Service;

import com.Lucroar.iQueue.Entity.DailyQueueSequence;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Objects;

@Service
public class DailySequenceGeneratorService {

    private final MongoOperations mongoOperations;

    public DailySequenceGeneratorService(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    public long generateDailySequence(String baseName) {
        String today = LocalDate.now().toString(); // e.g., "2025-04-15"
        String seqName = baseName + "_" + today;

        DailyQueueSequence counter = mongoOperations.findAndModify(
                Query.query(Criteria.where("_id").is(seqName)),
                new Update().inc("seq", 1),
                FindAndModifyOptions.options().returnNew(true).upsert(true),
                DailyQueueSequence.class);

        return Objects.nonNull(counter) ? counter.getSeq() : 1;
    }
}