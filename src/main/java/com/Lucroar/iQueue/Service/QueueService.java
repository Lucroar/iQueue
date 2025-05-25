package com.Lucroar.iQueue.Service;

import com.Lucroar.iQueue.DTO.CustomerDTO;
import com.Lucroar.iQueue.DTO.QueueDTO;
import com.Lucroar.iQueue.Entity.Customer;
import com.Lucroar.iQueue.Entity.QueueEntry;
import com.Lucroar.iQueue.Entity.Status;
import com.Lucroar.iQueue.Repository.CustomerRepository;
import com.Lucroar.iQueue.Repository.QueueRepository;
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
    private final InMemoryQueueService inMemoryQueueService;
    private final List<Integer> tableTiers = Arrays.asList(2, 4, 6);

    public QueueService(QueueRepository queueRepository,
                        CustomerRepository customerRepository,
                        DailySequenceGeneratorService sequenceGenerator,
                        InMemoryQueueService inMemoryQueueService) {
        this.queueRepository = queueRepository;
        this.customerRepository = customerRepository;
        this.sequenceGenerator = sequenceGenerator;
        this.inMemoryQueueService = inMemoryQueueService;
    }

    public QueueDTO  createQueue(Customer customer, QueueEntry queueEntry) {
        QueueDTO queueDTO = checkQueue(customer);
        if (queueDTO != null) return null;
        Customer customerCont = customerRepository.findByUsername(customer.getUsername()).get();
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setCustomerId(customerCont.getId());
        customerDTO.setUsername(customer.getUsername());

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
        List<Status> targetStatuses = List.of(Status.WAITING, Status.SEATED);
        Optional<QueueEntry> queueCont = queueRepository.findByCustomerUsernameAndStatusIn(customer.getUsername(), targetStatuses);
        return queueCont.map(QueueDTO::new).orElse(null);
    }

//    Used to be called after queue is created
//    public QueueDTO enterQueue(QueueDTO queue) {
//        Optional<QueueEntry> queueCont = queueRepository.findById(queue.getQueue_id());
//        if (queueCont.isPresent()) {
//            QueueEntry queueEntity = queueCont.get();
//            queueEntity.setStatus(Status.WAITING);
//            queueRepository.save(queueEntity);
//            return new QueueDTO(queueEntity);
//        }
//        return null;
//    }

    public QueueDTO cancelQueue(QueueDTO queue) {
        Optional<QueueEntry> queueCont = queueRepository.findById(queue.getQueue_id());
        if (queueCont.isPresent()) {
            QueueEntry queueEntryEntity = queueCont.get();
            queueEntryEntity.setStatus(Status.CANCELLED);
            queueRepository.save(queueEntryEntity);
            return new QueueDTO(queueEntryEntity);
        }
        return null;
    }

    //Mark the table as dirty and customer as done
    public QueueDTO finishedQueue(Customer customer) {
        Optional<QueueEntry> queueCont = queueRepository.findByCustomerUsernameAndStatusIn(customer.getUsername(), List.of(Status.SEATED));
        if (queueCont.isPresent()) {
            QueueEntry queueEntryEntity = queueCont.get();
            queueEntryEntity.setStatus(Status.DONE);
            inMemoryQueueService.releaseTable(queueEntryEntity.getTable_number());
            queueRepository.save(queueEntryEntity);
            return new QueueDTO(queueEntryEntity);

        }
        return null;
    }

    private int findAppropriateTableTier(int numPeople) {
        for (int tier : tableTiers) {
            if (numPeople <= tier) return tier;
        }
        return -1;
    }
}
