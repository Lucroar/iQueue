package com.Lucroar.iQueue.Service;

import com.Lucroar.iQueue.DTO.*;
import com.Lucroar.iQueue.Entity.*;
import com.Lucroar.iQueue.Repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class CashierMenuService {
    private final TableRepository tableRepository;
    private final QueueRepository queueRepository;
    private final OrdersRepository ordersRepository;
    private final OrdersHistoryRepository orderHistory;
    private final PaymentRepository paymentRepository;

    public CashierMenuService(TableRepository tableRepository, QueueRepository queueRepository, OrdersRepository ordersRepository, OrdersHistoryRepository orderHistory, PaymentRepository paymentRepository) {
        this.tableRepository = tableRepository;
        this.queueRepository = queueRepository;
        this.ordersRepository = ordersRepository;
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

        return orders.stream()
                    .sorted(Comparator.comparing(OrdersHistory::getTableNumber))
                    .map(order ->{
                    OrdersHistoryDTO dto = new OrdersHistoryDTO();
                    dto.setOrderDate(order.getOrderDate());
                    dto.setCustomer(order.getCustomer());
                    dto.setStatus(order.getStatus());
                    dto.setTableNumber(order.getTableNumber());
                    dto.setTotal(order.getTotal());
                    return dto;
        }).toList();
    }

    public Payment orderPayment(OrderPaymentDTO paymentDTO){
        Optional<OrdersHistory> history = orderHistory.findByCustomer_usernameAndStatus(paymentDTO.getUsername(), OrderStatus.UNPAID);
        if (history.isPresent()) {
            OrdersHistory order = history.get();
            Payment payment = new Payment();
            payment.setCustomer(order.getCustomer());
            payment.setOrderHistoryId(order.getId());
            payment.setAmount(order.getTotal());
            payment.setPaymentMethod(paymentDTO.getPaymentMethod());
            paymentRepository.save(payment);
            return payment;
        } else if (paymentDTO.isGuest()) {
            Payment payment = new Payment();
            Optional<OrdersHistory> ordersHistory = orderHistory.findByCustomer_CustomerIdAndStatus(payment.getCustomer().getUsername(), OrderStatus.UNPAID);
            Optional<Orders> orders = ordersRepository.findByCustomer_username(paymentDTO.getUsername());
            Optional<QueueEntry> entry = queueRepository.findByCustomer_Username(paymentDTO.getUsername());

            if (orders.isPresent() && ordersHistory.isPresent()) {
                payment.setCustomer(orders.get().getCustomer());
                payment.setOrderHistoryId(ordersHistory.get().getId());
                payment.setAmount(orders.get().getTotal());
                payment.setPaymentMethod(paymentDTO.getPaymentMethod());
                paymentRepository.save(payment);
            }

            return payment;
        }
        return null;
    }

    public boolean usernameForTakeOutIsExisting(String username) {
        Optional<Orders> orders = ordersRepository.findByCustomer_UsernameAndTakeOut(username, true);
        return orders.isPresent();
    }

}
