package com.Lucroar.iQueue.DTO;

import lombok.Data;

@Data
public class QueueCreationRequest {
    private int num_people;
    private String guestUsername;
    private String accessCode;
}
