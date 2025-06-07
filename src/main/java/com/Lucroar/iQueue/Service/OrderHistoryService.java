package com.Lucroar.iQueue.Service;

import com.Lucroar.iQueue.DTO.OrdersHistoryDTO;
import com.Lucroar.iQueue.Repository.OrdersHistoryRepository;
import com.Lucroar.iQueue.Repository.OrdersRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderHistoryService {
    private final OrdersHistoryRepository ordersHistoryRepository;
    public OrderHistoryService(OrdersHistoryRepository ordersHistoryRepository) {
        this.ordersHistoryRepository = ordersHistoryRepository;
    }

//    public List<OrdersHistoryDTO> viewCustomerHistory(){
//
//    }

}
