package com.Lucroar.iQueue.Service;

import com.Lucroar.iQueue.Entity.Table;
import com.Lucroar.iQueue.Repository.TableRepository;
import org.springframework.stereotype.Service;

@Service
public class TableService {
    private final TableRepository tableRepository;

    public TableService(TableRepository tableRepository) {
        this.tableRepository = tableRepository;
    }

    public Table addTable(Table table) {
        return tableRepository.save(table);
    }
}
