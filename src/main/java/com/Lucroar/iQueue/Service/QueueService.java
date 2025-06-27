package com.Lucroar.iQueue.Service;

import com.Lucroar.iQueue.DTO.CustomerDTO;
import com.Lucroar.iQueue.DTO.QueueCreationRequest;
import com.Lucroar.iQueue.DTO.QueueDTO;
import com.Lucroar.iQueue.Entity.*;
import com.Lucroar.iQueue.Repository.*;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class QueueService {
    private final QueueRepository queueRepository;
    private final CustomerRepository customerRepository;
    private final CartRepository cartRepository;
    private final OrdersHistoryRepository ordersHistoryRepository;
    private final QueueHistoryRepository queueHistoryRepository;
    private final DailySequenceGeneratorService sequenceGenerator;
    private final InMemoryQueueService inMemoryQueueService;
    private final List<Integer> tableTiers = Arrays.asList(2, 4, 6);
    @Getter
    private final String accessCode = "x9j3b7qt2a0e";

    public QueueService(QueueRepository queueRepository, CustomerRepository customerRepository,
                        CartRepository cartRepository, OrdersHistoryRepository ordersHistoryRepository,
                        QueueHistoryRepository queueHistoryRepository, DailySequenceGeneratorService sequenceGenerator,
                        InMemoryQueueService inMemoryQueueService) {
        this.queueRepository = queueRepository;
        this.customerRepository = customerRepository;
        this.cartRepository = cartRepository;
        this.ordersHistoryRepository = ordersHistoryRepository;
        this.queueHistoryRepository = queueHistoryRepository;
        this.sequenceGenerator = sequenceGenerator;
        this.inMemoryQueueService = inMemoryQueueService;
    }

    public QueueDTO createQueue(Customer customer, QueueCreationRequest queueRequest) {
        QueueDTO queueDTO = checkQueue(customer);
        if (queueDTO != null) return null;
        Customer customerCont = customerRepository.findByUsername(customer.getUsername()).get();
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setCustomerId(customerCont.getId());
        customerDTO.setUsername(customer.getUsername());

        QueueEntry queueEntry = new QueueEntry();
        queueEntry.setNum_people(queueRequest.getNum_people());
        queueEntry.setQueueing_number(sequenceGenerator.generateQueueCode(queueEntry.getNum_people()));
        queueEntry.setCustomer(customerDTO);
        queueEntry.setStatus(Status.WAITING);
        queueEntry.setWaiting_since(LocalDateTime.now());
        QueueEntry queueEntryEntity = queueRepository.save(queueEntry);
        inMemoryQueueService.enqueue(queueEntryEntity);
        QueueDTO queue = new QueueDTO(queueEntryEntity);
        queue.setTier(findAppropriateTableTier(queueEntry.getNum_people()));
        return queue;
    }

    //Check if there is an existing queue to a customer
    public QueueDTO checkQueue(Customer customer) {
        List<Status> targetStatuses = List.of(Status.WAITING,Status.CONFIRMING, Status.SEATED);
        Optional<QueueEntry> queueCont = queueRepository.findActiveNonGuestByUsername(customer.getUsername(), targetStatuses);
        return queueCont.map(QueueDTO::new).orElse(null);
    }

    public QueueDTO cancelQueue(Customer customer) {
        Optional<QueueEntry> queueCont = queueRepository.findByCustomerUsernameAndStatusIn(customer.getUsername(), List.of(Status.WAITING));
        if (queueCont.isPresent()) {
            QueueEntry queueEntryEntity = queueCont.get();
            queueRepository.delete(queueEntryEntity);
            queueEntryEntity.setStatus(Status.CANCELLED);
            queueHistoryRepository.save(new QueueHistory(queueEntryEntity));
            return new QueueDTO(queueEntryEntity);
        }
        return null;
    }

    public QueueDTO finishedQueue(Customer customer) {
        Optional<QueueEntry> queueCont = queueRepository.findByCustomerUsernameAndStatusIn(
                customer.getUsername(), List.of(Status.SEATED));

        Optional<List<OrdersHistory>> ordersHistoriesOpt = ordersHistoryRepository
                .findByCustomer_UsernameAndStatus(customer.getUsername(), OrderStatus.ORDERING);

        if (queueCont.isPresent()) {
            QueueEntry queueEntryEntity = queueCont.get();

            // Remove from queue, release table, mark done
            queueRepository.delete(queueEntryEntity);
            queueEntryEntity.setStatus(Status.DONE);
            inMemoryQueueService.releaseTable(queueEntryEntity.getTable_number());
            queueHistoryRepository.save(new QueueHistory(queueEntryEntity));

            // Clear cart
            Optional<Cart> cart = cartRepository.findByCustomer_Username(customer.getUsername());
            cart.ifPresent(cartRepository::delete);

            // Combine all order histories
            combineOrders(ordersHistoriesOpt, queueEntryEntity);

            return new QueueDTO(queueEntryEntity);
        }

        return null;
    }

    private void combineOrders(Optional<List<OrdersHistory>> ordersHistoriesOpt, QueueEntry queueEntryEntity) {
        if (ordersHistoriesOpt.isPresent()) {
            List<OrdersHistory> orderingHistories = ordersHistoriesOpt.get();

            List<Order> mergedOrders = new ArrayList<>();
            double total = 0.0;

            for (OrdersHistory history : orderingHistories) {
                for (Order order : history.getOrders()) {
                    Optional<Order> existing = mergedOrders.stream()
                            .filter(o -> o.getProduct_id().equals(order.getProduct_id()))
                            .findFirst();

                    if (existing.isPresent()) {
                        Order existingOrder = existing.get();
                        existingOrder.setQuantity(existingOrder.getQuantity() + order.getQuantity());
                    } else {
                        mergedOrders.add(new Order(order)); // Deep copy recommended
                    }

                    total += order.getPrice() * order.getQuantity();
                }

                // Delete individual history record
                ordersHistoryRepository.delete(history);
            }

            // Save new merged OrdersHistory as UNPAID
            OrdersHistory consolidated = new OrdersHistory(
                    orderingHistories.getFirst().getCustomer(),
                    mergedOrders,
                    OrderStatus.UNPAID,
                    LocalDateTime.now(),
                    queueEntryEntity.getTable_number()
            );
            consolidated.setTotal(total);
            ordersHistoryRepository.save(consolidated);
        }
    }


    //Create a random cashier based user for queue creation
    public QueueDTO cashierCreateQueue(QueueCreationRequest queueRequest) {
        boolean usernameExists = queueRepository.findByCustomerUsernameAndStatusIn(queueRequest.getGuestUsername(),
                List.of(Status.WAITING,Status.CONFIRMING,Status.SEATED)).isPresent();
        if (usernameExists) return null;
        QueueEntry queueEntry = new QueueEntry();
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setUsername(queueRequest.getGuestUsername());
        customerDTO.setGuest(true);
        queueEntry.setCustomer(customerDTO);

        queueEntry.setNum_people(queueRequest.getNum_people());
        queueEntry.setQueueing_number(sequenceGenerator.generateQueueCode(queueEntry.getNum_people()));
        queueEntry.setStatus(Status.WAITING);
        queueEntry.setWaiting_since(LocalDateTime.now());
        QueueEntry queueEntryEntity = queueRepository.save(queueEntry);
        inMemoryQueueService.enqueue(queueEntryEntity);
        QueueDTO queue = new QueueDTO(queueEntryEntity);
        queue.setTier(findAppropriateTableTier(queueEntry.getNum_people()));
        return queue;
    }

    private int findAppropriateTableTier(int numPeople) {
        for (int tier : tableTiers) {
            if (numPeople <= tier) return tier;
        }
        return -1;
    }

    public QueueDTO doneTable(CustomerDTO customerDTO) {
        Optional<QueueEntry> queueEntry = queueRepository.findByCustomerUsernameAndStatusIn(customerDTO.getUsername(), List.of(Status.SEATED));

        if (queueEntry.isPresent()) {
            QueueEntry entry = queueEntry.get();

            // Remove from queue and archive
            queueRepository.delete(entry);
            entry.setStatus(Status.DONE);
            queueHistoryRepository.save(new QueueHistory(entry));

            // Fetch all ORDERING OrdersHistory records for this customer
            Optional<List<OrdersHistory>> ordersHistoriesOpt =
                    ordersHistoryRepository.findByCustomer_UsernameAndStatus(customerDTO.getUsername(), OrderStatus.ORDERING);

            combineOrders(ordersHistoriesOpt, entry);

            inMemoryQueueService.releaseTable(entry.getTable_number());
            return new QueueDTO(entry);
        }

        return null;
    }

    public boolean existingOrderHistory(Customer customer){
        Optional<List<OrdersHistory>> historyOpt = ordersHistoryRepository.findByCustomer_UsernameAndStatus(customer.getUsername(), OrderStatus.UNPAID);
        return ordersHistoryRepository
                .findByCustomer_UsernameAndStatus(customer.getUsername(), OrderStatus.UNPAID)
                .map(list -> !list.isEmpty())
                .orElse(false);
    }

//    private CustomerDTO generateRandomGuestNumber(){
//        String randomCode = String.format("%04d", new Random().nextInt(10000)); // 0000 - 9999
//        CustomerDTO guest = new CustomerDTO();
//        guest.setUsername("Guest-" + randomCode);
//        guest.setGuest(true);
//        return guest;
//    }
}