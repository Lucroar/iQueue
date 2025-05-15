package com.Lucroar.iQueue.DTO;

import com.Lucroar.iQueue.Entity.QueueEntry;
import com.Lucroar.iQueue.Entity.Status;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QueueDTO {
    private String queue_id;
    private String queueing_number;
    private int num_people;
    private Status status;

    public QueueDTO(QueueEntry queueEntry) {
        this.queue_id = queueEntry.getQueue_id();
        this.queueing_number = queueEntry.getQueueing_number();
        this.num_people = queueEntry.getNum_people();
        this.status = queueEntry.getStatus();
    }
}
