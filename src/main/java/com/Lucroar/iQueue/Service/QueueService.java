package com.Lucroar.iQueue.Service;

import com.Lucroar.iQueue.DTO.CustomerDTO;
import com.Lucroar.iQueue.DTO.QueueDTO;
import com.Lucroar.iQueue.Entity.Customer;
import com.Lucroar.iQueue.Entity.Queue;
import com.Lucroar.iQueue.Entity.Status;
import com.Lucroar.iQueue.Repository.CustomerRepository;
import com.Lucroar.iQueue.Repository.QueueRepository;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class QueueService {
    private final QueueRepository queueRepository;
    private final CustomerRepository customerRepository;
    private final DailySequenceGeneratorService sequenceGenerator;

    public QueueService(QueueRepository queueRepository,
                        CustomerRepository customerRepository,
                        DailySequenceGeneratorService sequenceGenerator) {
        this.queueRepository = queueRepository;
        this.customerRepository = customerRepository;
        this.sequenceGenerator = sequenceGenerator;
    }

    //A qr code contains the number of table and the username
    public QueueDTO  createQueue(Customer customer, Queue queue) {
        if (checkQueue(customer) != null) return null;
        Customer customerCont = customerRepository.findByUsername(customer.getUsername()).get();
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setCustomer_id(customerCont.getCustomer_id());
        customerDTO.setUsername(customer.getUsername());

        queue.setQueueing_number((int) sequenceGenerator.generateDailySequence("queue_sequence"));
        queue.setCustomer(customerDTO);
        queue.setStatus(Status.CREATED);
        queue.setWaiting_since(LocalDateTime.now());
        Queue queueEntity = queueRepository.save(queue);
        return new QueueDTO(queueEntity);
    }

    //Check if there is an existing queue to a customer
    public QueueDTO checkQueue(Customer customer) {
        List<Status> targetStatuses = Arrays.asList(Status.CREATED, Status.WAITING);
        Optional<Queue> queueCont = queueRepository.findByCustomerUsernameAndStatusIn(customer.getUsername(), targetStatuses);
        return queueCont.map(QueueDTO::new).orElse(null);
    }

    public QueueDTO enterQueue(QueueDTO queue) {
        Optional<Queue> queueCont = queueRepository.findById(queue.getQueue_id());
        if (queueCont.isPresent()) {
            Queue queueEntity = queueCont.get();
            queueEntity.setStatus(Status.WAITING);
            queue.setStatus(queueEntity.getStatus());
            return queue;
        }
        return null;
    }
}
