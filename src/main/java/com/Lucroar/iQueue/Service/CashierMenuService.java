package com.Lucroar.iQueue.Service;

import com.Lucroar.iQueue.DTO.CashierMainMenuDTO;
import com.Lucroar.iQueue.DTO.OrdersHistoryDTO;
import com.Lucroar.iQueue.Entity.*;
import com.Lucroar.iQueue.Repository.OrdersHistoryRepository;
import com.Lucroar.iQueue.Repository.PaymentRepository;
import com.Lucroar.iQueue.Repository.QueueRepository;
import com.Lucroar.iQueue.Repository.TableRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CashierMenuService {
    private final TableRepository tableRepository;
    private final QueueRepository queueRepository;
    private final OrdersHistoryRepository orderHistory;
    private final PaymentRepository paymentRepository;

    public CashierMenuService(TableRepository tableRepository, QueueRepository queueRepository, OrdersHistoryRepository orderHistory, PaymentRepository paymentRepository) {
        this.tableRepository = tableRepository;
        this.queueRepository = queueRepository;
        this.orderHistory = orderHistory;
        this.paymentRepository = paymentRepository;
    }

    public List<CashierMainMenuDTO> viewListOfTables(){
        List<Table> tables = tableRepository.findAll();
        List<QueueEntry> entries = queueRepository.findByStatusIn(List.of(Status.SEATED, Status.CONFIRMING));

        List<CashierMainMenuDTO> result = new ArrayList<>();

        for (Table table : tables) {
            QueueEntry match = entries.stream()
                    .filter(e -> e.getTable_number() == table.getTableNumber()).
                    findFirst().orElse(null);

            CashierMainMenuDTO dto = new CashierMainMenuDTO();
            dto.setTableNumber(table.getTableNumber());
            dto.setSize(table.getSize());
            dto.setStatus(table.getStatus());

            if (match != null) {
                dto.setUsername(match.getCustomer().getUsername());
            }
            result.add(dto);
        }
        return result;
    }

    public List<OrdersHistoryDTO> viewAllUnpaid(){
        List<OrdersHistory> orders = orderHistory.findByStatus(OrderStatus.UNPAID);

        return orders.stream().map(order ->{
           OrdersHistoryDTO dto = new OrdersHistoryDTO();
           dto.setOrderDate(order.getOrderDate());
            dto.setCustomer(order.getCustomer());
            dto.setStatus(order.getStatus());
            dto.setTableNumber(order.getTableNumber());
            dto.setTotal(order.getTotal());
            return dto;
        }).toList();
    }

    public Payment orderPayment(OrdersHistoryDTO ordersHistory){
        Optional<OrdersHistory> history = orderHistory.findByCustomer_usernameAndStatus(ordersHistory.getCustomer().getUsername(), OrderStatus.ORDERING);
        if (history.isPresent()) {
            OrdersHistory order = history.get();
            Payment payment = new Payment();
            payment.setCustomer(order.getCustomer());
            payment.setOrders(order.getOrders());
            payment.setOrderHistoryId(order.getId());
            payment.setAmount(order.getTotal());
            payment.setPaymentMethod(ordersHistory.getPaymentMethod());
            paymentRepository.save(payment);
            return payment;
        }
        return null;
    }

}
