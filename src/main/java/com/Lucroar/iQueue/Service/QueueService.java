package com.Lucroar.iQueue.Service;

import com.Lucroar.iQueue.DTO.CustomerDTO;
import com.Lucroar.iQueue.DTO.QueueDTO;
import com.Lucroar.iQueue.Entity.Customer;
import com.Lucroar.iQueue.Entity.Queue;
import com.Lucroar.iQueue.Entity.Status;
import com.Lucroar.iQueue.Repository.QueueRepository;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class QueueService {
    private final QueueRepository queueRepository;
    private final DailySequenceGeneratorService sequenceGenerator;

    public QueueService(QueueRepository queueRepository, DailySequenceGeneratorService sequenceGenerator) {
        this.queueRepository = queueRepository;
        this.sequenceGenerator = sequenceGenerator;
    }

    //A qr code contains the number of table and the username
    public Queue enterQueue(Customer customer, QueueDTO queue) {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setCustomer_id(customer.getCustomer_id());
        customerDTO.setUsername(customer.getUsername());

        Queue queueEntity = new Queue();
        queueEntity.setQueueing_number((int) sequenceGenerator.generateDailySequence("queue_sequence"));
        queueEntity.setCustomer(customerDTO);
        queueEntity.setStatus(Status.WAITING);
        queueEntity.setWaiting_since(LocalDateTime.now());
        queueEntity.setNum_people(queue.getNum_people());
        return queueRepository.save(queueEntity);
    }
}
