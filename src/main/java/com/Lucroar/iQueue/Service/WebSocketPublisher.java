package com.Lucroar.iQueue.Service;

import com.Lucroar.iQueue.DTO.SeatedTableInfo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketPublisher {
    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketPublisher(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendSeatedTableInfo(String tier, SeatedTableInfo info) {
        messagingTemplate.convertAndSend("/topic/seated-tables/" + tier, info);
    }

}
