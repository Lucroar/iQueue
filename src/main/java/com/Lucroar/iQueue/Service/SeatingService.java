package com.Lucroar.iQueue.Service;

import com.Lucroar.iQueue.DTO.CashierMainMenuDTO;
import com.Lucroar.iQueue.DTO.CustomerDTO;
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
    private final WebSocketPublisher webSocketPublisher;

    public SeatingService(TableRepository tableRepository, QueueRepository queueRepository, WebSocketPublisher webSocketPublisher) {
        this.tableRepository = tableRepository;
        this.queueRepository = queueRepository;
        this.webSocketPublisher = webSocketPublisher;
    }

    public CashierMainMenuDTO confirmSeating(CustomerDTO customer){
        List<Status> targetStatus = List.of(Status.CONFIRMING);
        Optional<QueueEntry> queueCont = queueRepository.findByCustomerUsernameAndStatusIn(customer.getUsername(), targetStatus);

        if(queueCont.isPresent()){
            QueueEntry queueEntry = queueCont.get();
            Table table = tableRepository.findByTableNumber(queueEntry.getTable_number());
            table.setStatus(Status.OCCUPIED);
            queueEntry.setStatus(Status.SEATED);
            tableRepository.save(table);
            queueRepository.save(queueEntry);
            CashierMainMenuDTO mainMenuDTO = new CashierMainMenuDTO(table.getTableNumber(), queueEntry.getCustomer().getUsername(), table.getSize(), table.getStatus());
            webSocketPublisher.sendSeatedTableInfoToCashier(mainMenuDTO);
            return mainMenuDTO;
        } else {
            return null;
        }
    }

    //find by table and mark as available
    public CashierMainMenuDTO missedSeating(CustomerDTO customer){
        List<Status> targetStatus = List.of(Status.CONFIRMING);
        Optional<QueueEntry> queueCont = queueRepository.findByCustomerUsernameAndStatusIn(customer.getUsername(), targetStatus);
        if(queueCont.isPresent()){
            QueueEntry queueEntry = queueCont.get();
            Table table = tableRepository.findByTableNumber(queueEntry.getTable_number());
            table.setStatus(Status.AVAILABLE);
            queueEntry.setStatus(Status.MISSED);
            queueRepository.save(queueEntry);
            tableRepository.save(table);
            CashierMainMenuDTO mainMenuDTO = new CashierMainMenuDTO(table.getTableNumber(), null, table.getSize(), table.getStatus());
            webSocketPublisher.sendSeatedTableInfoToCashier(mainMenuDTO);
            return mainMenuDTO;
        } else {
            return null;
        }
    }
}
