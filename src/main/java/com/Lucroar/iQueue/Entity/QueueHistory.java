package com.Lucroar.iQueue.Entity;

import com.Lucroar.iQueue.DTO.CustomerDTO;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "QueueHistory")
@Data
public class QueueHistory {
    @Id
    private String queue_id;
    private CustomerDTO customer;
    private String queueing_number;
    private Status status;
    private LocalDateTime waiting_since;
    private int num_people;
    private LocalDateTime time_seated;
    private int table_number;

    public QueueHistory(QueueEntry queueEntry) {
        queue_id = queueEntry.getQueue_id();
        customer = queueEntry.getCustomer();
        queueing_number = queueEntry.getQueueing_number();
        status = queueEntry.getStatus();
        waiting_since = queueEntry.getWaiting_since();
        num_people = queueEntry.getNum_people();
        time_seated = queueEntry.getTime_seated();
        table_number = queueEntry.getTable_number();
    }
}
