package com.Lucroar.iQueue.Service;

import com.Lucroar.iQueue.DTO.CashierMainMenuDTO;
import com.Lucroar.iQueue.Entity.QueueEntry;
import com.Lucroar.iQueue.Entity.Status;
import com.Lucroar.iQueue.Entity.Table;
import com.Lucroar.iQueue.Repository.QueueRepository;
import com.Lucroar.iQueue.Repository.TableRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CashierMenuService {
    private final TableRepository tableRepository;
    private final QueueRepository queueRepository;

    public CashierMenuService(TableRepository tableRepository, QueueRepository queueRepository) {
        this.tableRepository = tableRepository;
        this.queueRepository = queueRepository;
    }

    public List<CashierMainMenuDTO> viewListOfTables(){
        List<Table> tables = tableRepository.findAll();
        List<QueueEntry> entries = queueRepository.findByStatus(Status.SEATED);

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

}
