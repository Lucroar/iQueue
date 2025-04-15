package com.Lucroar.iQueue.Entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "DailyQueueSequence")
@Data
public class DailyQueueSequence {
    @Id
    private String id;
    private long seq;
}
