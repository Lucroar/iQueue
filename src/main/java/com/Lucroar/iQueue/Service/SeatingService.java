package com.Lucroar.iQueue.Service;

import com.Lucroar.iQueue.DTO.CustomerDTO;
import com.Lucroar.iQueue.DTO.QueueDTO;
import com.Lucroar.iQueue.Entity.Customer;
import com.Lucroar.iQueue.Entity.QueueEntry;
import com.Lucroar.iQueue.Entity.Status;
import com.Lucroar.iQueue.Entity.Table;
import com.Lucroar.iQueue.Repository.QueueRepository;
import com.Lucroar.iQueue.Repository.TableRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SeatingService {
    private final TableRepository tableRepository;
    private final QueueRepository queueRepository;

    public SeatingService(TableRepository tableRepository, QueueRepository queueRepository) {
        this.tableRepository = tableRepository;
        this.queueRepository = queueRepository;
    }

    public QueueDTO confirmSeating(CustomerDTO customer){
        List<Status> targetStatus = List.of(Status.CONFIRMING);
        Optional<QueueEntry> queueCont = queueRepository.findByCustomerUsernameAndStatusIn(customer.getUsername(), targetStatus);

        if(queueCont.isPresent()){
            QueueEntry queueEntry = queueCont.get();
            Table table = tableRepository.findByTableNumber(queueEntry.getTable_number());
            table.setStatus(Status.OCCUPIED);
            queueEntry.setStatus(Status.SEATED);
            tableRepository.save(table);
            queueRepository.save(queueEntry);
            return new QueueDTO(queueEntry);
        } else {
            return null;
        }
    }

    public QueueDTO missedSeating(CustomerDTO customer){
        List<Status> targetStatus = List.of(Status.CONFIRMING);
        Optional<QueueEntry> queueCont = queueRepository.findByCustomerUsernameAndStatusIn(customer.getUsername(), targetStatus);
        if(queueCont.isPresent()){
            QueueEntry queueEntry = queueCont.get();
            queueEntry.setStatus(Status.MISSED);
            queueRepository.save(queueEntry);
            return new QueueDTO(queueEntry);
        } else {
            return null;
        }
    }
}
