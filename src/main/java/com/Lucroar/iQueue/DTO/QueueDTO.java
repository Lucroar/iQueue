package com.Lucroar.iQueue.DTO;

import com.Lucroar.iQueue.Entity.Queue;
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
    private int num_people;
    private Status status;

    public QueueDTO(Queue queue) {
        this.queue_id = queue.getId();
        this.num_people = queue.getNum_people();
        this.status = queue.getStatus();
    }
}
