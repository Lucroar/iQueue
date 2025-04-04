package com.Lucroar.iQueue.Entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "Queue")
@Data
public class Queue {
    @Id
    private Id queue_id;
    private Customer customer;
    private Status status;
    private LocalDateTime waiting_since;
    private int num_people;
}