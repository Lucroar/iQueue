package com.Lucroar.iQueue.DTO;

import com.Lucroar.iQueue.Entity.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueueDTO {
    private int queue_id;
    private int num_people;
    private Status status;
}
