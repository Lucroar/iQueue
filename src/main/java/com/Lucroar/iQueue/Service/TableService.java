package com.Lucroar.iQueue.Service;

import com.Lucroar.iQueue.Entity.Status;
import com.Lucroar.iQueue.Entity.Table;
import com.Lucroar.iQueue.Repository.TableRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TableService {
    private final TableRepository tableRepository;

    public TableService(TableRepository tableRepository) {
        this.tableRepository = tableRepository;
    }

    public Table addTable(Table table) {
        return tableRepository.save(table);
    }

    public List<Table> getAllTable(){
        return tableRepository.findAll();
    }

    public List<Table> getDirtyTable(){
        return tableRepository.findByStatus(Status.DIRTY);
    }

    public Table cleanedTable(int tableNumber){
        Table table = tableRepository.findByTableNumber(tableNumber);
        table.setStatus(Status.AVAILABLE);
        tableRepository.save(table);
        return table;
    }
}
