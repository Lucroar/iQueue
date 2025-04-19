package com.Lucroar.iQueue.Service;

import com.Lucroar.iQueue.Entity.DailyQueueSequence;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Service
public class DailySequenceGeneratorService {

    private final MongoOperations mongoOperations;

    public DailySequenceGeneratorService(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    public String generateQueueCode(int pax) {
        String prefix;

        if (pax == 1) {
            prefix = "S";
        } else if (pax <= 3) {
            prefix = "T";
        } else {
            prefix = "F";
        }

        String today = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        String seqName = prefix + "_" + today;

        DailyQueueSequence counter = mongoOperations.findAndModify(
                Query.query(Criteria.where("_id").is(seqName)),
                new Update().inc("seq", 1),
                FindAndModifyOptions.options().returnNew(true).upsert(true),
                DailyQueueSequence.class
        );

        long sequence = Objects.nonNull(counter) ? counter.getSeq() : 1;
        String formattedSeq = String.format("%03d", sequence); // e.g., "001"
        return prefix + formattedSeq;
    }

}