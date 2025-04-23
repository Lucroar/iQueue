package com.Lucroar.iQueue.Entity;

import com.Lucroar.iQueue.DTO.CustomerDTO;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "Reservation")
@Data
public class Reservation {
    @Id
    private String reservation_id;
    private int table_number;
    private CustomerDTO customer;
    private LocalDateTime reservation_date;
    private int num_people;
    private Status status;
}
