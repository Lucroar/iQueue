package com.Lucroar.iQueue.Service;

import com.Lucroar.iQueue.DTO.CashierMainMenuDTO;
import com.Lucroar.iQueue.DTO.SeatedTableInfo;
import com.Lucroar.iQueue.DTO.TableOrderDTO;
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

    public void sendSeatedTableInfoToCashier(CashierMainMenuDTO cashierMainMenu) {
        messagingTemplate.convertAndSend("/topic/seated-tables/", cashierMainMenu);
    }

    public void sendTableOrders(TableOrderDTO tableOrderDTO) {
        messagingTemplate.convertAndSend("/topic/table/orders", tableOrderDTO);
    }

}
