package com.Lucroar.iQueue.Entity;

import com.Lucroar.iQueue.DTO.CustomerDTO;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "Queue")
@Data
public class Queue {
    @Id
    private String id;
    private CustomerDTO customer;
    private int queueing_number;
    private Status status;
    private LocalDateTime waiting_since;
    private int num_people;
}